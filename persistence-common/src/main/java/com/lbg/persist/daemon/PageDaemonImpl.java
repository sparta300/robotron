package com.lbg.persist.daemon;

import static com.lbg.persist.daemon.PageConstants.NONE;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.file.FileMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.lbg.persist.SafeCast;
import com.lbg.utility.PropertyMap;


/**
 * an implementation of PageDaemon.
 * 
 * @author C006011
 */
public class PageDaemonImpl implements PageDaemon
{
	private static final Logger log = LoggerFactory.getLogger(PageDaemonImpl.class);	
	
	private final Map<Long, Page> lookUp;
	private final EvictionAlgorithm algorithm;
	private final List<Page> pages;
	private final List<Boolean> pinned;
	private final int pageLimit;
	
	@Inject
	public PageDaemonImpl(EvictionAlgorithm algorithm, PropertyMap props)
	{
		this.algorithm = algorithm;
		this.pageLimit = props.getInteger("page.limit");

		pages = new ArrayList<Page>(pageLimit);
		lookUp = new HashMap<Long, Page>(pageLimit);
		pinned = new ArrayList<Boolean>(pageLimit);

		// spin through and mark the entries as available
		for (int e = 0; e < pageLimit; e++)
		{
			pages.add(null);
			pinned.add(Boolean.FALSE);
		}
	}

	@Override
	public Page make(PageIdentifier pageId) throws PhysicalResourceException
	{
		log.debug("make(pos=" + pageId.getPosition() + ")");

		RepositoryFile repo = pageId.getFile();
		long startPosition = pageId.getPosition();
		int byteCount = pageId.getByteCount();

		Page page = lookUp.get(startPosition);

		if (page != null)
		{
			throw new IllegalArgumentException("cannot make an existing page");
		}

		if (repo.getFileMode() == FileMode.READ_ONLY)
		{
			throw new IllegalArgumentException("cannot make a new page in a read-only file");
		}

		if ((startPosition % byteCount) != 0)
		{
			throw new IllegalArgumentException("start position not aligned to a page boundary");
		}

		final Page newPage = pageIn(pageId);
		newPage.getByteBuffer().clear();
		return newPage;
	}

	public Page fetch(PageIdentifier pageId) throws PhysicalResourceException
	{
		log.debug("fetch(pos=" + pageId.getPosition() + ")");
		final Page page = lookUp.get(pageId.getPosition());

		if (page != null)
		{
			page.getByteBuffer().position(0);
			return page;
		}

		return pageIn(pageId);
	}

	public Page pageIn(PageIdentifier pageId) throws PhysicalResourceException
	{
		log.debug("pageIn(pos=" + pageId.getPosition() + ")");
		
		// get a free slot - by evicting one that is already used if necessary
		final int index = free();
		final MappedByteBuffer mbb = getMappedBuffer(pageId);
		return loadPage(pageId, index, mbb);
	}
		 
	
	/**
	 * creates a mapped byte buffer for a single block.
	 *  
	 * @param pageId
	 * @return
	 * @throws PhysicalResourceException
	 */
	private MappedByteBuffer getMappedBuffer(PageIdentifier pageId) throws PhysicalResourceException
	{
		final RepositoryFile repo = pageId.getFile();
		final long startPosition = pageId.getPosition();
		final int pageSize = pageId.getByteCount();

		try
		{
			return repo.map(startPosition, pageSize);
		}
		catch (IOException ioe)
		{
			log.error("could not map repo file", ioe);
			throw new PhysicalResourceException("could not map repo file", ioe);
		}
	}
	
	private Page loadPage(PageIdentifier pageId, int index, MappedByteBuffer mbb) throws PhysicalResourceException
	{
		long fileSize = -1L;
		
		try
		{
			fileSize = pageId.getFile().size();
		}
		catch (IOException ioe)
		{
			throw new PhysicalResourceException("could not determine file size", ioe);
		}

		// this creates a new buffer that shares the underlying contents but has its 
		// own independent position, limit and mark
		final ByteBuffer dup = mbb.duplicate();

		// set our position to the start of this block
		// dup.position(SafeCast.fromLongToInt(startPosition));
		dup.position(0);

		// set the limit to the end of this block
		// int limit = SafeCast.fromLongToInt(startPosition + byteCount);
		final int limit = SafeCast.fromLongToInt(pageId.getByteCount());

		// if the file has not been written to an exact block size (this is a
		// configuration option), limit the buffer to the file
		// size rather than the expected end of block. Otherwise you will create
		// problems with read only stores. If you create
		// a mapped buffer bigger than the current file size, you need write
		// permission.
		if (limit > SafeCast.fromLongToInt(fileSize))
		{
			dup.limit();
		}
		else
		{
			dup.limit(limit);
		}

		final CacheBuffer buffer = new CacheBufferImpl(mbb, dup.slice());
		final Page page = new PageImpl(pageId, buffer, index);
		algorithm.acquire(index);
		lookUp.put(pageId.getPosition(), page);
		pages.set(index, page);
		return page;
	}

	@Override
	public void pageOut(Page page) throws PageException
	{
		log.debug("pageOut(slot=" + page.getIndex() + ",pos=" + page.getIdentifier().getPosition() + ")");
		checkPinned(page.getIndex(), "cannot page out a pinned page");
		page.flush();
	}

	@Override
	public void flushAll()
	{
		log.debug("flushAll()");

		for (Page page : pages)
		{
			if (page != null)
			{
				page.flush();
			}
		}
	}

	@Override
	public void pin(Page page)
	{
		log.debug("pin(slot=" + page.getIndex() + ",pos=" + page.getIdentifier().getPosition() + ")");
		pinned.set(page.getIndex(), Boolean.TRUE);
	}

	@Override
	public void unpin(Page page)
	{
		log.debug(String.format("unpin(slot=%d,pos=%d)", page.getIndex(), page.getIdentifier().getPosition()));
		pinned.set(page.getIndex(), Boolean.FALSE);
	}

	/**
	 * frees up a slot for use.
	 * 
	 * @return the index of the free slot
	 * @throws PageException
	 */
	private int free() throws PageException
	{
		final int freeSlot = findFreeSlot();

		if (freeSlot != NONE)
		{
			return freeSlot;
		}

		// otherwise evict a page
		final int victimIndex = algorithm.select(this, pages);
		final Page page = pages.get(victimIndex);
		evict(victimIndex, page);
		return victimIndex;
	}

	private int findFreeSlot()
	{
		for (int b = 0; b < pageLimit; b++)
		{
			if (pages.get(b) == null)
			{
				log.debug("page slot " + b + " is available");
				return b;
			}
		}

		log.debug("no page index available");
		return NONE;
	}

	private void checkPinned(int victimIndex, String errorMessage) throws PageException
	{
		final Boolean isPinned = pinned.get(victimIndex);

		if (isPinned == null)
		{
			throw new PageException("cannot determine if page is pinned");
		}

		if (isPinned.booleanValue())
		{
			throw new PageException(errorMessage);
		}
	}

	public boolean isPinned(int index) throws PageException
	{
		final Boolean isPinned = pinned.get(index);

		if (isPinned == null)
		{
			throw new PageException("cannot determine if page is pinned");
		}

		return isPinned;
	}

	private void evict(int victimIndex, Page victim) throws PageException
	{
		log.debug("evicting cache entry in slot " + victimIndex);

		checkPinned(victimIndex, "cannot evict a pinned page");

		victim.flush();
		algorithm.release(victimIndex);
		pages.set(victimIndex, null);
	}

	public int getPageLimit()
	{
		return pageLimit;
	}
}

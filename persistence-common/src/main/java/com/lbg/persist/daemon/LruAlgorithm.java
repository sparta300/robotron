/*
 * Created on Apr 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.lbg.persist.daemon;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.utility.PropertyMap;

/**
 * A cache algorithm which will select the least recently used cache entry for
 * eviction.
 * 
 * @author C006011
 */
public class LruAlgorithm implements EvictionAlgorithm
{
	private static final Logger log = LoggerFactory.getLogger(LruAlgorithm.class);
	
	private final List<PageEntry> entries;
	private final int pageLimit;
	private final Provider<PageEntry> entryFactory;
	
	@Inject
	public LruAlgorithm(PropertyMap props, Provider<PageEntry> pageFactory)
	{
		this.pageLimit = props.getInteger("page.limit");
		this.entryFactory = pageFactory;
		
		entries = new ArrayList<PageEntry>(pageLimit);

		for (int p = 0; p < pageLimit; p++)
		{
			entries.add(null);
		}
	}

	@Override
	public void acquire(int index)
	{
		entries.set(index, entryFactory.get());
	}

	@Override
	public void release(int index)
	{
		entries.set(index, null);
	}

	@Override
	public int select(AlgorithmCallback callback, List<Page> pages)	throws PageException
	{
		long oldestTime = 0L;
		int oldestIndex = -1;

		for (int e = 0; e < pageLimit; e++)
		{
			Page page = pages.get(e);

			if (page != null)
			{
				PageEntry entry = entries.get(e);
				long time = entry.getLastUseTimeStamp();

				if (!callback.isPinned(e) && time > oldestTime)
				{
					oldestTime = time;
					oldestIndex = e;
				}
			}
		}

		if (oldestIndex == -1)
		{
			throw new PageException("select failed - the cache must be entry or no time stamps are set");
		}

		log.debug("select(" + oldestIndex + ")");
		return oldestIndex;
	}
}

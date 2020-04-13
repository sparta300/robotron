package org.hydroid.beowulf.model.list;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.hydroid.beowulf.model.list.manager.LinkedListSegmentManager;
import org.hydroid.beowulf.model.list.manager.SegmentAllocation;
import org.hydroid.beowulf.model.list.manager.SinglyLinkedListManager;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHead;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lbg.resource.ResourceEmpty;

public class LifoLinkedListModelImpl implements PersistentLinkedListModel
{
	private static final Logger logger = LoggerFactory.getLogger(LifoLinkedListModelImpl.class);

	private final LocatorFactory locatorFactory;
	private final ListStorageApi api;
	private final SinglyLinkedListHead runtime;
	private final SinglyLinkedListManager headManager;

	public LifoLinkedListModelImpl(long headSlotLocator, ListStorageApi api, LocatorFactory locatorFactory) throws PhysicalResourceException
	{
		this.api = api;
		this.locatorFactory = locatorFactory;
		headManager = api.getManager(headSlotLocator);
		runtime = headManager.getRuntime();
	}

	@Override
	public void add(byte[] item) throws PhysicalResourceException
	{
		if (!hasHead())
		{
			addHead(item);
			return;
		}

		pushHead(item);
	}

	private void addHead(byte[] data) throws PhysicalResourceException
	{
		SegmentAllocation allocation = allocateSegment(data);
		runtime.setHeadSegmentLocator(allocation.getLocator());
	}

	private void pushHead(byte[] data) throws PhysicalResourceException
	{
		// allocate space for the new segment
		SegmentAllocation allocation = allocateSegment(data);
		SinglyLinkedListSegment newHead = allocation.getSegment();

		// load the head
		long headLocator = runtime.getHeadSegmentLocator();

		// do the manipulation
		newHead.setNextSegmentLocator(headLocator);
		runtime.setHeadSegmentLocator(allocation.getLocator());
	}

	private SegmentAllocation allocateSegment(byte[] data)
			throws PhysicalResourceException
	{
		long dataLocator = api.storeSegmentData(data);
		SegmentAllocation allocation = allocateSegment();
		SinglyLinkedListSegment segment = allocation.getSegment();
		segment.setDataLocator(dataLocator);
		incrementLength();

		if (logger.isDebugEnabled())
		{
			String segmentMnemonic = locatorFactory.make(allocation.getLocator()).asAddress();
			String dataMnemonic = locatorFactory.make(dataLocator).asAddress();
			logger.debug(String.format("allocateSegment(%s) data stored in %s", segmentMnemonic, dataMnemonic));
		}

		return allocation;
	}

	private void incrementLength()
	{
		int current = runtime.getLength();
		runtime.setLength(current + 1);
	}

	private SegmentAllocation allocateSegment() throws PhysicalResourceException
	{
		final SegmentAllocation allocation = headManager.allocateSegment();

		if (allocation != null)
		{
			return allocation;
		}

		final LinkedListSegmentManager segmentManager = findFreeSegmentManager();
		return segmentManager.allocateSegment();
	}

	private LinkedListSegmentManager findFreeSegmentManager() throws PhysicalResourceException
	{
		if (headManager.hasFreeSegment())
		{
			return headManager;
		}

		// otherwise grab a new slot and return a segment manager for it.
		return api.newSinglyLinkedListSegment();
	}

	@Override
	public Locator getMetaData()
	{
		return headManager.getMetaData();
	}

	@Override
	public boolean isEmpty()
	{
		return !hasHead();
	}

	@Override
	public Iterator<Locator> iterate() throws PhysicalResourceException
	{
		final int size = runtime.getLength();

		if (size == 0)
		{
			return Collections.<Locator> emptyList().iterator();
		}

		final List<Locator> list = new ArrayList<Locator>(size);
		long current = runtime.getHeadSegmentLocator();

		for (int s = 0; s < size; s++)
		{
			SinglyLinkedListSegment segment = api.getSegment(current);
			list.add(locatorFactory.make(segment.getDataLocator()));
			current = segment.getNextSegmentLocator();
		}

		return Collections.unmodifiableList(list).iterator();
	}

	@Override
	public void remove() throws PhysicalResourceException
	{
		if (isEmpty())
		{
			throw new ResourceEmpty();
		}

		// load the head
		final long headLocator = runtime.getHeadSegmentLocator();
		final SinglyLinkedListSegment head = api.getSegment(headLocator);

		// do the manipulation
		runtime.setHeadSegmentLocator(head.getNextSegmentLocator());
	}

	@Override
	public void removeAll()
	{

	}

	@Override
	public void setMetaData(Locator locator)
	{
		headManager.setMetaData(locator);
	}

	@Override
	public int size()
	{
		return runtime.getLength();
	}

	@Override
	public byte[] take() throws PhysicalResourceException
	{
		if (isEmpty())
		{
			throw new ResourceEmpty();
		}

		// load the head
		final long headLocator = runtime.getHeadSegmentLocator();
		final SinglyLinkedListSegment head = api.getSegment(headLocator);
		final byte[] data = api.getSegmentData(head.getDataLocator());

		return data;
	}

	public byte[] peek() throws PhysicalResourceException
	{
		if (isEmpty())
		{
			throw new ResourceEmpty();
		}

		// load the head
		final long headLocator = runtime.getHeadSegmentLocator();
		final SinglyLinkedListSegment head = api.getSegment(headLocator);

		return api.getSegmentData(head.getDataLocator());
	}

	@Override
	public byte[] getSegmentData(Locator locator) throws PhysicalResourceException
	{
		if (locator == null)
		{
			throw new IllegalArgumentException("lifo list get segment data: locator cannot be null");
		}

		return api.getSegmentData(locator.asLong());
	}

	private boolean hasHead()
	{
		return runtime.getHeadSegmentLocator() != UNSET_LOCATOR;
	}
}

package org.hydroid.beowulf.model.list.manager;

import org.hydroid.beowulf.manager.FreeList256ManagerImpl;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHead;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHeadSlot;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinglyLinkedListManager extends AbstractLinkedListSegmentManager {
	
	public SinglyLinkedListManager(long blockId, SlotOverhead so, SinglyLinkedListHeadSlot headSlot, 
			                       LocatorFactory locatorFactory) {
		super(blockId, so, headSlot.getSegments(), new FreeList256ManagerImpl(headSlot.getFreeList()), locatorFactory);
		this.blockId = blockId;
		this.so = so;
		this.locatorFactory = locatorFactory;
		this.headSlot = headSlot;
		runtime = headSlot.getRuntime();
		logger.debug("SinglyLinkedListManager");
	}
	
	public void setMetaData(Locator locator) {
		so.setMetaDataLocator(locator.asLong());
	}
	
	public Locator getMetaData() {
		return locatorFactory.make(so.getMetaDataLocator());
	}

	public long getLocator() { return so.getLocator(); }
	public SinglyLinkedListHead getRuntime() { return runtime; }
	public long getBlockId() { return blockId; }
	public SlotOverhead getSlotOverhead() { return so; }
	public int getSlotId() { return so.getSlotIndex(); }
	public SinglyLinkedListHeadSlot getHeadSlot() { return headSlot; }

	
	private final SinglyLinkedListHeadSlot headSlot;
	private final long blockId;
	private final SinglyLinkedListHead runtime;
	private final SlotOverhead so;
	private final LocatorFactory locatorFactory;
	 
	private static final Logger logger = LoggerFactory.getLogger(SinglyLinkedListManager.class);




}

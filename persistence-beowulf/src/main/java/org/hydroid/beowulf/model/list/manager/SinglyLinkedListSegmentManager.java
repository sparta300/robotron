package org.hydroid.beowulf.model.list.manager;

import org.hydroid.beowulf.manager.FreeList256ManagerImpl;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegmentSlot;
import org.hydroid.beowulf.overlay.FreeListRuntime;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SinglyLinkedListSegmentManager extends AbstractLinkedListSegmentManager {
	public SinglyLinkedListSegmentManager(long blockId, SlotOverhead so, SinglyLinkedListSegmentSlot slot, LocatorFactory locatorFactory) {
		super(blockId, so, slot.getSegments(), new FreeList256ManagerImpl(slot.getFreeList()), locatorFactory);
		this.slot = slot;
		logger.debug("SinglyLinkedListSegmentManager");
		
	}
	
	public FreeListRuntime getFreeListRuntime() { return slot.getFreeList().getRuntime(); }
	
	private static final Logger logger = LoggerFactory.getLogger(SinglyLinkedListSegmentManager.class);
	
	private final SinglyLinkedListSegmentSlot slot;


}

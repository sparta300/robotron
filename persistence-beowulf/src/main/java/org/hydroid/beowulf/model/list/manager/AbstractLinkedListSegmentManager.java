package org.hydroid.beowulf.model.list.manager;

import static org.hydroid.beowulf.BeowulfConstants.NO_FREE_SLOT;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hydroid.beowulf.manager.FreeList256Manager;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.overlay.FreeListRuntime;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;

abstract public class AbstractLinkedListSegmentManager implements LinkedListSegmentManager {
	public AbstractLinkedListSegmentManager(long blockId, SlotOverhead so, SinglyLinkedListSegment[] segments, 
			                                FreeList256Manager freeListManager, LocatorFactory locatorFactory) {
		this.blockId = blockId;
		this.slotId = so.getSlotIndex();
		this.locatorFactory = locatorFactory;
		this.segments = segments;
		this.flm = freeListManager;
	}
	
	public SegmentAllocation allocateSegment() {
		int freeSlotIndex = flm.findFreeSlot();
		
		if (freeSlotIndex == NO_FREE_SLOT) {
			return null;
		}
				
		flm.allocateSlot(freeSlotIndex);
		Locator locator = locatorFactory.make(blockId, slotId, freeSlotIndex);
		logger.debug(String.format("allocateSegment(%s)", locator.asAddress()));
		return new SegmentAllocation(locator, segments[freeSlotIndex]);
	}
	
	public void deallocateSegment(int subslotIndex) {
		flm.deallocateSlot(subslotIndex);
		SinglyLinkedListSegment segment = getSegment(subslotIndex);
		segment.setDataLocator(UNSET_LOCATOR);
		segment.setNextSegmentLocator(UNSET_LOCATOR);
	}	
	
	public boolean hasFreeSegment() {
		int freeSlotIndex = flm.findFreeSlot();
		
		if (freeSlotIndex == NO_FREE_SLOT) {
			return false;
		}
		
		return true;
	}
	
	public Map<Integer, SinglyLinkedListSegment> getUsedSegments () {
		int[] usedSegmentIds = flm.getFreeSlotManager().findUsedSlots();
		int count = usedSegmentIds.length;
		
		if (count == 0) {
			return Collections.emptyMap();
		}
		
		Map<Integer, SinglyLinkedListSegment> map = new HashMap<Integer, SinglyLinkedListSegment>();
		
		for (int id : usedSegmentIds) {
			map.put(id, segments[id]);
		}
		
		return map;
	}
	

	public SinglyLinkedListSegment[] getSegments() { return segments; }
	public SinglyLinkedListSegment getSegment(int index) { return segments[index]; }
	public FreeListRuntime getFreeListRuntime() { return flm.getFreeListRuntime(); }	
	public FreeList256Manager getFreeListManager() { return flm; }	
	
	private long blockId;
	private int slotId;
	private LocatorFactory locatorFactory;
	private FreeList256Manager flm;	
	private final SinglyLinkedListSegment[] segments;
	
	private static final Logger logger = LoggerFactory.getLogger(AbstractLinkedListSegmentManager.class);
}

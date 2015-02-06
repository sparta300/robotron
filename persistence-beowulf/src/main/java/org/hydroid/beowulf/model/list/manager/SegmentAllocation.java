package org.hydroid.beowulf.model.list.manager;

import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.storage.Locator;

public class SegmentAllocation {
	public SegmentAllocation(Locator locator, SinglyLinkedListSegment segment){
		this.locator = locator;
		this.segment = segment;
	}
	
	public SinglyLinkedListSegment getSegment() { return segment; }
	public int getSubslotIndex() { return locator.getIndex(); }
	public long getBlockId() { return locator.getBlockId(); }
	public int getSlotId() { return locator.getStructureId(); }
	public long getLocator() { return locator.asLong(); }

	private SinglyLinkedListSegment segment;
	private Locator locator;
}

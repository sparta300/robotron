package org.hydroid.beowulf.model.list.overlay;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.overlay.CompositeOverlay;
import org.hydroid.beowulf.overlay.FreeList256;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

/**
 * a slot overlay for a singly linked list non-head segment.
 * 
 * @author smiley
 */
public class SinglyLinkedListSegmentSlot extends CompositeOverlay {
	
	public SinglyLinkedListSegmentSlot(ByteBuffer bb, OverlayFactory overlayFactory, LocatorFactory locatorFactory) {
		super(bb, overlayTypes, overlayFactory, locatorFactory);
		freeList = getComponent("fl-256");
		int segmentCount = freeList.getRuntime().getTotal();
		segments = new SinglyLinkedListSegment[segmentCount];
		
		// eager load all the segments
		for (int s = 0; s < segmentCount; s++) {
			int segmentIndex = add(bb, "ll-seg1", overlayFactory);
			segments[s] = getComponent(segmentIndex);
		}
		
		markEnd();
	}

	public SinglyLinkedListSegment[] getSegments() { return segments; }
	public SinglyLinkedListSegment getSegment(int index) { return segments[index]; }
	public FreeList256 getFreeList() { return freeList; }
	
	private FreeList256 freeList;
	private SinglyLinkedListSegment[] segments;
    private static final List<String> overlayTypes = Arrays.asList("fl-256");
}

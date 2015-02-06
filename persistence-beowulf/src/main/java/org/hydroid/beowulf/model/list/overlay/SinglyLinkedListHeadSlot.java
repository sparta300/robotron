package org.hydroid.beowulf.model.list.overlay;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.overlay.CompositeOverlay;
import org.hydroid.beowulf.overlay.FreeList256;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

/**
 * a slot overlay for a singly linked list head segment.
 * 
 * @author smiley
 */
public class SinglyLinkedListHeadSlot extends CompositeOverlay {

	public SinglyLinkedListHeadSlot(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory) {
		super(bb, overlayTypes, factory, locatorFactory);
		FreeList256 freeList256 = getComponent("fl-256");
		int segmentCount = freeList256.getRuntime().getTotal();
		segments = new SinglyLinkedListSegment[segmentCount];
		
		// eager load all the segments
		for (int s = 0; s < segmentCount; s++) {
			int segmentIndex = add(bb, "ll-seg1", factory);
			segments[s] = getComponent(segmentIndex);
		}
		
		markEnd();
		runtime = getComponent("ll-htl");
		freeList = getComponent("fl-256");
	}
	
	public SinglyLinkedListSegment[] getSegments() { return segments; }
	public SinglyLinkedListSegment getSegment(int index) { return segments[index]; }
	
	public SinglyLinkedListHead getRuntime() { return runtime; }
	public FreeList256 getFreeList() { return freeList; }
	
	private SinglyLinkedListHead runtime;
	private FreeList256 freeList;
	private SinglyLinkedListSegment[] segments;
    private static final List<String> overlayTypes = Arrays.asList("ll-htl", "fl-256");
}

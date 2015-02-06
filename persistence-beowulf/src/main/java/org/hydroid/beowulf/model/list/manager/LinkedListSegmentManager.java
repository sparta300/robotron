package org.hydroid.beowulf.model.list.manager;

import java.util.Map;

import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.overlay.FreeListRuntime;

public interface LinkedListSegmentManager {
	boolean hasFreeSegment();
	SegmentAllocation allocateSegment();
	SinglyLinkedListSegment getSegment(int index);
	SinglyLinkedListSegment[] getSegments();
	Map<Integer, SinglyLinkedListSegment> getUsedSegments();
	FreeListRuntime getFreeListRuntime();
}

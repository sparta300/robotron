package org.hydroid.beowulf.model.list;

import org.hydroid.beowulf.model.list.manager.SinglyLinkedListManager;
import org.hydroid.beowulf.model.list.manager.SinglyLinkedListSegmentManager;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;

import com.lbg.resource.PhysicalResourceException;

public interface ListStorageApi {
	SinglyLinkedListSegment getSegment(long locator) throws PhysicalResourceException;
	SinglyLinkedListManager getManager(long locator) throws PhysicalResourceException;
	SinglyLinkedListManager newSinglyLinkedList() throws PhysicalResourceException;
	SinglyLinkedListSegmentManager newSinglyLinkedListSegment() throws PhysicalResourceException;
	long storeSegmentData(byte[] data) throws PhysicalResourceException;
	byte[] getSegmentData(long locator) throws PhysicalResourceException;
}

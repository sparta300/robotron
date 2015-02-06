package org.hydroid.beowulf.manager;

import org.hydroid.beowulf.model.list.ListStorageApi;
import org.hydroid.beowulf.model.list.PersistentLinkedListModel;

import com.lbg.resource.PhysicalResourceException;

public interface SandpitManager {
	PersistentLinkedListModel getLinkedList(ListStorageApi api) throws PhysicalResourceException;
}

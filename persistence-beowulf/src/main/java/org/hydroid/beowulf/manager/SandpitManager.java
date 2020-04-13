package org.hydroid.beowulf.manager;

import org.hydroid.beowulf.model.list.ListStorageApi;
import org.hydroid.beowulf.model.list.PersistentLinkedListModel;

import org.hydroid.file.PhysicalResourceException;

public interface SandpitManager {
	PersistentLinkedListModel getLinkedList(ListStorageApi api) throws PhysicalResourceException;
}

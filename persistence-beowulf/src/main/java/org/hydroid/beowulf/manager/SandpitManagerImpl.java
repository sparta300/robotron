package org.hydroid.beowulf.manager;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import org.hydroid.beowulf.model.list.LifoLinkedListModelImpl;
import org.hydroid.beowulf.model.list.ListStorageApi;
import org.hydroid.beowulf.model.list.PersistentLinkedListModel;
import org.hydroid.beowulf.model.list.manager.SinglyLinkedListManager;
import org.hydroid.beowulf.overlay.Sandpit;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;



public class SandpitManagerImpl implements SandpitManager {
	private final LocatorFactory locatorFactory;
	private final Sandpit sandpit;
	
	public SandpitManagerImpl(Sandpit sandpit, LocatorFactory locatorFactory) {
		this.sandpit = sandpit;
		this.locatorFactory = locatorFactory;
	}
	
	public PersistentLinkedListModel getLinkedList(ListStorageApi api) throws PhysicalResourceException {
		long locator = sandpit.getLinkedListLocator();
		
		// if the list does not exist, return a model that can be used to read it
		if (locator != UNSET_LOCATOR) {
			return new LifoLinkedListModelImpl(locator, api, locatorFactory);
		}
		
		// otherwise create a new one and put its locator in the sandpit
		SinglyLinkedListManager headManager = api.newSinglyLinkedList();
		locator = headManager.getLocator();
		sandpit.setLinkedListLocator(locator);
		return new LifoLinkedListModelImpl(locator, api, locatorFactory);
	}

	public Sandpit getSandpit() { return sandpit; }
}

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
	public SandpitManagerImpl(Sandpit sandpit, LocatorFactory locatorFactory) {
		this.sandpit = sandpit;
		this.locatorFactory = locatorFactory;
	}
	
	public PersistentLinkedListModel getLinkedList(ListStorageApi api) throws PhysicalResourceException {
		long locator = sandpit.getLinkedListLocator();
		
		if (locator != UNSET_LOCATOR) {
			return new LifoLinkedListModelImpl(locator, api, locatorFactory);
		}
		
		SinglyLinkedListManager headManager = api.newSinglyLinkedList();
		locator = headManager.getLocator();
		sandpit.setLinkedListLocator(locator);
		return new LifoLinkedListModelImpl(locator, api, locatorFactory);
	}

	public Sandpit getSandpit() { return sandpit; }
	
	private LocatorFactory locatorFactory;
	private Sandpit sandpit;
}

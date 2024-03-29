package org.hydroid.beowulf;

import org.hydroid.beowulf.manager.SandpitManager;
import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.RepositoryOverhead;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.space.SpaceManagementContext;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;



public interface RepositoryManager {
	StorageManager getStorageManager(long blockId) throws PhysicalResourceException;
	
	/**
	 * makes a new storage unit by creating a new page in the store.
	 * 
	 * @return a storage manager attached to the new storage block
	 * @throws PhysicalResourceException
	 */
	StorageManager makeStorage() throws PhysicalResourceException;
	
	LocatorFactory getLocatorFactory();
	
	Sizing getSizing();

	SandpitManager getSandpitManager();

	void shutdown() throws PhysicalResourceException ;
	
	public long getBlockCount();
	
	public int getPageCacheSize();
	
	SpaceManagementContext getSpaceManagementContext();
	
	RepositoryOverhead getRepositoryOverhead();
	
	MetaData getMetaData();
}

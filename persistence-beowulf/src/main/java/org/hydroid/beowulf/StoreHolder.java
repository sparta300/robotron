package org.hydroid.beowulf;

import java.io.File;

/**
 * holds all the details for a store for a {@link LocationManager}.
 * 
 * @author smiley
 */
class StoreHolder {
	StoreHolder(String storeName, String suffix, File file) {
		this.storeName = storeName;
		this.suffix = suffix;
		this.file = file;
	}
			
	void setRepositoryManager(RepositoryManager manager) { this.manager = manager; }		
	RepositoryManager getManager() { return manager; }
	File getFile() { return file; }
	String getStoreName() { return storeName; }
	String getSuffix() { return suffix; }
	
	private RepositoryManager manager;
	private final String storeName;
	private final File file;
	private final String suffix;
}
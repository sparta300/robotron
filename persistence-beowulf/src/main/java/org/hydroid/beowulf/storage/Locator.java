package org.hydroid.beowulf.storage;

public interface Locator {

	long getBlockId();

	int getStructureId();

	int getIndex();
	
	String asAddress();
	
	long asLong();
	
	
}

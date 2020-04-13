package org.hydroid.beowulf;

import org.hydroid.file.PhysicalResourceException;

public interface LocationManagerApi

{
	void create(String url) throws PhysicalResourceException;

	RepositoryManager read(String url) throws PhysicalResourceException;

	RepositoryManager update(String url) throws PhysicalResourceException;

	void remove(String url) throws PhysicalResourceException;

	void shutDown() throws PhysicalResourceException;
	
	Iterable<String> iterateStoreNames();
}

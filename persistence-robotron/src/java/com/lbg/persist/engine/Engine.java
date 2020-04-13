package com.lbg.persist.engine;

import org.hydroid.file.PhysicalResourceException;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.PersistenceException;

public interface Engine
{
	void up();
	
	void open(RepositoryFile file) throws PhysicalResourceException, EngineException, PersistenceException;

	void down();
}

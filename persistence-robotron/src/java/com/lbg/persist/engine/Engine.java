package com.lbg.persist.engine;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.PersistenceException;
import com.lbg.resource.PhysicalResourceException;

public interface Engine
{
	void up();
	
	void open(RepositoryFile file) throws PhysicalResourceException, EngineException, PersistenceException;

	void down();
}

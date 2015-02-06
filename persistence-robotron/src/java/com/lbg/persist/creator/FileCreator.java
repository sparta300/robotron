package com.lbg.persist.creator;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.PersistenceException;

public interface FileCreator
{
	void createFile(RepositoryFile file, int blockSize,	int maxBlockCount) throws PersistenceException;
}

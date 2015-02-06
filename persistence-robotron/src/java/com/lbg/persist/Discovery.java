package com.lbg.persist;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.engine.Engine;

public interface Discovery
{
	Engine getEngineFor(RepositoryFile file);
}

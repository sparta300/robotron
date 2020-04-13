package com.lbg.persist;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.engine.Engine;

public interface Discovery
{
	Engine getEngineFor(RepositoryFile file);
}

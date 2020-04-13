package com.lbg.persist;

import javax.inject.Inject;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.engine.Engine;
import com.lbg.persist.structure.raw.VersionNumber;

/**
 * the idea here is to create some forward compatibility by using the version number.
 * 
 * @author C006011
 */
public class DiscoveryImpl implements Discovery
{
	private final Engine engine;
	
	@Inject
	private DiscoveryImpl(Engine engine)
	{
		this.engine = engine;
	}

	@Override
	public Engine getEngineFor(RepositoryFile file)
	{
		return engine;
	}
 	
	public Engine getEngineFor(VersionNumber version)
	{
		return engine;
	}
}

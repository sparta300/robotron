package com.lbg.module;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.lbg.persist.Discovery;
import com.lbg.persist.DiscoveryImpl;
import com.lbg.persist.Swizzler;
import com.lbg.persist.SwizzlerImpl;
import com.lbg.persist.engine.Engine;
import com.lbg.persist.engine.RobotronEngine;
import com.lbg.persist.engine.TranslationLookasideBuffer;
import com.lbg.persist.engine.TranslationLookasideBufferImpl;
import com.lbg.persist.engine.service.ServiceRegistry;
import com.lbg.persist.engine.service.ServiceRegistryImpl;
import com.lbg.persist.structure.BlockReader;
import com.lbg.persist.structure.BlockReaderImpl;

public class EngineModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		bind(Swizzler.class).to(SwizzlerImpl.class).in(Singleton.class);
		bind(Discovery.class).to(DiscoveryImpl.class).in(Singleton.class);
		bind(Engine.class).to(RobotronEngine.class).in(Singleton.class);
		bind(BlockReader.class).to(BlockReaderImpl.class).in(Singleton.class);
		bind(ServiceRegistry.class).to(ServiceRegistryImpl.class).in(Singleton.class);
		bind(TranslationLookasideBuffer.class).to(TranslationLookasideBufferImpl.class).in(Singleton.class);
	}

}

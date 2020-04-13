package com.lbg.module;

import com.google.inject.AbstractModule;
import com.lbg.persist.daemon.EvictionAlgorithm;
import com.lbg.persist.daemon.LruAlgorithm;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageDaemonImpl;

/**
 * 
 * @author C006011
 */
public class PageDaemonModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(PageDaemon.class).to(PageDaemonImpl.class).asEagerSingleton();
		bind(EvictionAlgorithm.class).to(LruAlgorithm.class).asEagerSingleton(); 
	}
}

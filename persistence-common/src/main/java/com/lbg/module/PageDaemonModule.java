package com.lbg.module;

import org.hydroid.page.EvictionAlgorithm;
import org.hydroid.page.LruAlgorithm;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageDaemonImpl;

import com.google.inject.AbstractModule;

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

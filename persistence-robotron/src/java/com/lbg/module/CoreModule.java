package com.lbg.module;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.lbg.persist.StringCodec;
import com.lbg.persist.StringCodecImpl;

public class CoreModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		bind(StringCodec.class).to(StringCodecImpl.class).in(Singleton.class);		
	}

}

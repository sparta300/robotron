package com.lbg.module;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.lbg.persist.creator.FileCreator;
import com.lbg.persist.creator.ManifestCreator;
import com.lbg.persist.engine.service.Service;
import com.lbg.persist.structure.ListHelper;
import com.lbg.persist.structure.ListHelperImpl;
import com.lbg.persist.telemetry.ExecutorBasedFrameGenerator;
import com.lbg.persist.telemetry.FrameGenerator;
import com.lbg.persist.telemetry.FrameSink;
import com.lbg.persist.telemetry.FrameSinkImpl;
import com.lbg.persist.telemetry.TelemetryService;
import com.lbg.utility.DateTimeFactory;
import com.lbg.utility.DateTimeFactoryImpl;

public class TelemetryModule extends AbstractModule
{
	@Override
	protected void configure()
	{
		bind(ListHelper.class).to(ListHelperImpl.class).in(Singleton.class);
		
		bind(FrameSink.class).to(FrameSinkImpl.class).in(Singleton.class);
		
		//bind(FileCreator.class).to(TelemetryFileCreator.class).in(Singleton.class);
		bind(FileCreator.class).to(ManifestCreator.class).in(Singleton.class);
		
		bind(Executor.class).annotatedWith(Names.named("subscribers")).toInstance(Executors.newCachedThreadPool());
		bind(FrameGenerator.class).to(ExecutorBasedFrameGenerator.class).in(Singleton.class);
		bind(DateTimeFactory.class).to(DateTimeFactoryImpl.class).in(Singleton.class);
		
		bind(Service.class).annotatedWith(Names.named("telemetry")).to(TelemetryService.class);
	}
}

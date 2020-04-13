package com.lbg.persist.main;

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.google.inject.Module;
import org.hydroid.file.RepositoryFile;
import com.lbg.module.CoreModule;
import com.lbg.module.EngineModule;
import com.lbg.module.PageDaemonModule;
import com.lbg.module.PropertyModule;
import com.lbg.module.StructureModule;
import com.lbg.module.TelemetryModule;
import com.lbg.persist.Discovery;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.engine.Engine;
import com.lbg.persist.engine.EngineException;
import com.lbg.utility.PropertyMap;

/**
 * launches an engine that can read a telemetry file.
 * 
 * @author C006011
 */
public class EngineLauncher
{
	private static final Logger log = LoggerFactory.getLogger(EngineLauncher.class);	
	private static final String PROPERTY_FILE = "src/resource/creator.properties";

	private final Discovery discovery;
	
	@Inject
	private EngineLauncher(Discovery discovery, PropertyMap props)
	{
		this.discovery = discovery;
	}
		
	private void launch(String[] args) throws PhysicalResourceException, PersistenceException, EngineException
	{
		int argumentCount = args.length;

		if (argumentCount != 1)
		{
			System.err.println(String.format("usage: java %s fileName", getClass().getName()));
			return;
		}

		String fileName = args[0];
		RepositoryFile file = null;
		
		try
		{
			file = new RepositoryFile(new File("src/resource"), fileName, ".telemetry", "rw");
		}
		catch (FileNotFoundException e)
		{
			log.error("could not create the repository file", e);
			return;
		}

		final Engine engine = discovery.getEngineFor(file);
		engine.up();
		engine.open(file);
		
		try
		{
			TimeUnit.SECONDS.sleep(30);
		}
		catch (InterruptedException e)
		{
			log.warn("something interrupted the main thread: ignoring");
		}
		
		engine.down();
	}
	
	public static void main(String[] args)
	{
		log.info("creating injector ...");
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new PropertyModule(PROPERTY_FILE));
		modules.add(new PageDaemonModule());
		modules.add(new TelemetryModule());
		modules.add(new StructureModule());
		modules.add(new EngineModule());
		modules.add(new CoreModule());
		final Injector injector = createInjector(modules);
		final EngineLauncher launcher = injector.getInstance(EngineLauncher.class);	
		
		try
		{
			launcher.launch(args);
		}
		catch (Exception e)
		{
			log.error("launch exception", e);
		}
	}
}

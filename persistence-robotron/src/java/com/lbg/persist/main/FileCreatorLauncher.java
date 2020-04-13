package com.lbg.persist.main;

import static com.google.inject.Guice.createInjector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
import com.lbg.persist.PersistenceException;
import com.lbg.persist.creator.FileCreator;

/**
 * a launcher for classes that create files.
 * 
 * @author C006011
 */
public class FileCreatorLauncher
{
	private static final Logger log = LoggerFactory.getLogger(FileCreatorLauncher.class);
	
	private static final String SUFFIX = ".telemetry";
	private static final String FOLDER = "src/resource";
	private static final String PROPERTY_FILE = FOLDER + "/creator.properties";
	
	private final FileCreator fileCreator;
	
	@Inject
	private FileCreatorLauncher(FileCreator fileCreator)
	{
		this.fileCreator = fileCreator;
	}
	
	private void create(String[] args)
	{
		int argumentCount = args.length;

		if (argumentCount != 3)
		{
			System.err.println(String.format("usage: java %s fileName blockSize maxBlockCount", getClass().getName()));
			return;
		}

		final String fileName = args[0];
		final String blockSizeString = args[1];
		final String maxBlockCountString = args[2];
		final int blockSize = Integer.parseInt(blockSizeString);
		final int maxBlockCount = Integer.parseInt(maxBlockCountString);
		
		RepositoryFile file = null;
		
		try
		{
			file = new RepositoryFile(new File(FOLDER), fileName, SUFFIX, "rw");
		}
		catch (FileNotFoundException e)
		{
			log.error("could not create the repository file", e);
			return;
		}
		 	
		try
		{
			fileCreator.createFile(file, blockSize, maxBlockCount);
		}
		catch (PersistenceException e)
		{
			log.error("problem with file creator", e);
		}
	}
	 	
	
	public static void main(String[] args)
	{
		log.info("creating injector for launcher ...");
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new PropertyModule(PROPERTY_FILE));
		modules.add(new PageDaemonModule());
		modules.add(new TelemetryModule());
		modules.add(new StructureModule());
		modules.add(new EngineModule());
		modules.add(new CoreModule());
		final Injector injector = createInjector(modules);
		final FileCreatorLauncher launcher = injector.getInstance(FileCreatorLauncher.class);	
		
		try
		{
			launcher.create(args);
		}
		catch (Exception e)
		{
			log.error("launch exception", e);
		}
	}
}

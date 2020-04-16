package org.hydroid.beowulf.tool;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.creator.RootBlockCreator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import org.hydroid.file.RepositoryFile;
import com.lbg.module.PageDaemonModule;
import com.lbg.module.PropertyModule;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;

public class StoreCreator
{
	private static final Logger log = LoggerFactory.getLogger(StoreCreator.class);
	
	private static final String PROPERTY_FILE = "src/resource/creator.properties";
	
	private final PageDaemon daemon;
	private final LocatorFactory locatorFactory;
	
	@Inject
	private StoreCreator(PageDaemon pageDaemon, LocatorFactory locatorFactory)
	{
		this.daemon = pageDaemon;
		this.locatorFactory = locatorFactory;
	}
	
	private void create(String[] args)
	{
		int argumentCount = args.length;

		if (argumentCount != 3)
		{
			System.err.println(String.format("usage: java %s blockSize slotSize fileName", getClass().getName()));
			throw new IllegalArgumentException();
		}

		String blockSizeString = args[0];
		String slotSizeString = args[1];
		String fileName = args[2];
		
		int blockSize = Integer.parseInt(blockSizeString);
		int slotSize = Integer.parseInt(slotSizeString);
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
		
		final PageIdentifier page0 = new PageIdentifier(file, 0, blockSize);
		final OverlayFactory forge  = new OverlayFactory(true, locatorFactory);
		final StoreCreatorContext context = new StoreCreatorContext(daemon, file, page0, forge, locatorFactory, blockSize, slotSize);

		create(context);
	}

	public static void main(String[] args)
	{
		log.info("creating injector ...");
		
		final Injector injector = createInjector(new PropertyModule(PROPERTY_FILE), new PageDaemonModule());
		final StoreCreator launcher = injector.getInstance(StoreCreator.class);	
		
		try
		{
			launcher.create(args);
		}
		catch (Exception e)
		{
			log.error("launch exception", e);
		}
	}

	
	private void create(StoreCreatorContext context)
	{
		// peel some values out of the context
		PageDaemon daemon = context.getDaemon();
		RepositoryFile repo = context.getRepo();
		PageIdentifier pageId = context.getPageId();
		OverlayFactory factory = context.getForge();
		int blockSize = context.getBlockSize();
		int slotSize = context.getSlotSize();
		LocatorFactory locatorFactory = context.getLocatorFactory();

		Page page = null;

		try
		{
			page = daemon.make(pageId);
			ByteBuffer bb = page.getByteBuffer();

			
			RootBlockCreator root = new RootBlockCreator(bb, factory, locatorFactory, blockSize, slotSize);
			root.report();

		}
		catch (PageException e)
		{
			log.error("page exception on page-in", e);
		}
		catch (PhysicalResourceException pre)
		{
			log.error("failed to create root block", pre);
		}
		finally
		{
			daemon.flushAll();

			try
			{
				repo.close();
			}
			catch (IOException e)
			{
				log.error("i/o exception closing repo");
			}
		}
	}
}

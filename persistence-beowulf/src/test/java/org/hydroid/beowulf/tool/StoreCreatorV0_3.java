package org.hydroid.beowulf.tool;

import static com.google.inject.Guice.createInjector;
import static com.mfdev.utility.SmileyPlaces.UI_BEOWULF_RUNNER_RESOURCES;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.creator.RootBlockCreator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;
import com.lbg.module.PropertyModule;

public class StoreCreatorV0_3 {
	private static final Logger log = LoggerFactory.getLogger(StoreCreatorV0_3.class);

	private static final String PROPERTY_FILE = UI_BEOWULF_RUNNER_RESOURCES + "/creator.nv";

	private final PageDaemon daemon;
	private final LocatorFactory locatorFactory;

	@Inject
	private StoreCreatorV0_3(PageDaemon pageDaemon, LocatorFactory locatorFactory) {
		this.daemon = pageDaemon;
		this.locatorFactory = locatorFactory;
	}

	private void create(String[] args) {
		int argumentCount = args.length;

		if (argumentCount != 4) {
			System.err.println(String.format("usage: java %s blockSize slotSize fileName", getClass().getName()));
			return;
		}

		final String blockSizeString = args[0];
		final String slotSizeString = args[1];
		final String fileName = args[2];
		String suffix = args[3];

		if (!suffix.startsWith(".")) {
			suffix = "." + suffix;
		}

		int index = fileName.indexOf(suffix);
		final String targetFile = (index == -1) ? fileName : fileName.substring(0, index);

		log.debug("will create file " + UI_BEOWULF_RUNNER_RESOURCES + "/" + targetFile + suffix);
		int blockSize = Integer.parseInt(blockSizeString);
		int slotSize = Integer.parseInt(slotSizeString);
		RepositoryFile file = null;

		try {
			file = new RepositoryFile(new File(UI_BEOWULF_RUNNER_RESOURCES), targetFile, suffix, "rw");
		} catch (FileNotFoundException e) {
			log.error("could not create the repository file", e);
			return;
		}

		final PageIdentifier page0 = PageIdentifier.forRootBlock(file, blockSize);
		final OverlayFactory forge = new OverlayFactory(true, locatorFactory);
		final StoreCreatorContext context = new StoreCreatorContext(daemon, file, page0, forge, locatorFactory,	blockSize, slotSize);

		create(context);
	}

	public static void main(String[] args) {
		log.info("creating injector ...");

		final Injector injector = createInjector(new PropertyModule(PROPERTY_FILE), new StoreCreatorModule());
		final StoreCreatorV0_3 launcher = injector.getInstance(StoreCreatorV0_3.class);

		try {
			launcher.create(args);
		} catch (Exception e) {
			log.error("launch exception", e);
		}
	}

	// 
	private void create(StoreCreatorContext context) {
		// peel some values out of the context
		PageDaemon daemon = context.getDaemon();
		RepositoryFile repo = context.getRepo();
		PageIdentifier pageId = context.getPageId();
		OverlayFactory factory = context.getForge();
		int blockSize = context.getBlockSize();
		int slotSize = context.getSlotSize();
		LocatorFactory locatorFactory = context.getLocatorFactory();

		Page page = null;

		try {
			page = daemon.make(pageId);
			ByteBuffer bb = page.getByteBuffer();

			RootBlockCreator root = new RootBlockCreator(bb, factory, locatorFactory, blockSize, slotSize);
			root.report();

		} catch (PageException e) {
			log.error("page exception on page-in", e);
		} catch (PhysicalResourceException pre) {
			log.error("failed to create root block", pre);
		} finally {
			daemon.flushAll();

			try {
				repo.close();
			} catch (IOException e) {
				log.error("i/o exception closing repo");
			}
		}
	}
}

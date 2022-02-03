package org.hydroid.beowulf.tool;

import static com.google.inject.Guice.createInjector;
import static com.mfdev.utility.SmileyPlaces.UI_BEOWULF_RUNNER_RESOURCES;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.RootBlock;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.FileMode;
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

public class StoreReader {
	private static final Logger log = LoggerFactory.getLogger(StoreReader.class);

	private static final String PROPERTY_FILE = UI_BEOWULF_RUNNER_RESOURCES + "/reader.nv";

	private final PageDaemon daemon;
	private final LocatorFactory locatorFactory;

	@Inject
	private StoreReader(PageDaemon pageDaemon, LocatorFactory locatorFactory) {
		this.daemon = pageDaemon;
		this.locatorFactory = locatorFactory;
	}

	private void create(String[] args) throws PhysicalResourceException {
		int argumentCount = args.length;

		if (argumentCount != 2) {
			System.err.println(String.format("usage: java %s storeFileName storeFileSuffix", getClass().getName()));
			return;
		}

		String fileName = args[0];
		String suffix = args[1];

		if (!suffix.startsWith(".")) {
			suffix = "." + suffix;
		}

		int index = fileName.indexOf(suffix);
		final String targetFile = (index == -1) ? fileName : fileName.substring(0, index);

		log.debug("will read file " + UI_BEOWULF_RUNNER_RESOURCES + "/" + targetFile + suffix);
		RepositoryFile repoFile = null;

		try {
			repoFile = new RepositoryFile(new File(UI_BEOWULF_RUNNER_RESOURCES), targetFile, suffix, FileMode.READ_ONLY.symbol());
		} catch (FileNotFoundException e) {
			log.error("could not open the repository file", e);
			return;
		}

		log.debug("file opened successfully");
		ByteBuffer bb = null;
		
		try {
			bb = repoFile.read(48);
		} catch (IOException e) {
			throw new PhysicalResourceException("could not sniff enough bytes to determine the sizing information for the store file", e);			
		}
		
		MetaData metadata = new MetaData(bb, locatorFactory);
		log.debug("meta-data " + metadata.toString());
				
		Sizing sizing = new Sizing(bb, locatorFactory);
		read(repoFile, metadata, sizing);
	}


	private void read(RepositoryFile repoFile, MetaData metadata, Sizing sizing) {
		int blockSize = sizing.getBlockSize();
		int slotSize = sizing.getSlotSize();
		log.debug("block size " + blockSize);
		log.debug("slot size " + slotSize);

		PageIdentifier page0 = PageIdentifier.forRootBlock(repoFile, blockSize);
		OverlayFactory overlayFactory  = new OverlayFactory(false, locatorFactory);

		Page page = null;

		try {
			page = daemon.pageIn(page0);
			ByteBuffer bb = page.getByteBuffer();

			RootBlock root = new RootBlock(bb, overlayFactory, locatorFactory);
			root.report();
			
			long nextBlockId = root.getRepositoryOverhead().getNextBlockId() - 1L;
			log.debug("next block ID " + nextBlockId);
			
			for (long block = 1; block < nextBlockId; block++) {
				PageIdentifier blockPageId = page0.forBlock(block);
				daemon.fetch(blockPageId);
			}
		} catch (PageException e) {
			log.error("page exception on page-in", e);
		} catch (PhysicalResourceException pre) {
			log.error("failed to create root block", pre);
		} finally {
			daemon.flushAll();

			try {
				repoFile.close();
			} catch (IOException e) {
				log.error("i/o exception closing repo");
			}
		}
	}
	
	public static void main(String[] args) {
		log.info("creating injector ...");

		final Injector injector = createInjector(new PropertyModule(PROPERTY_FILE), new StoreReaderModule());
		final StoreReader launcher = injector.getInstance(StoreReader.class);

		try {
			launcher.create(args);
		} catch (Exception e) {
			log.error("launch exception", e);
		}
	}
}

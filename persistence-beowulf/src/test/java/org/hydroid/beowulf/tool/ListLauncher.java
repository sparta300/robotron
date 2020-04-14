package org.hydroid.beowulf.tool;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hydroid.beowulf.RepositoryManager;
import org.hydroid.beowulf.manager.SandpitManager;
import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.overlay.Sandpit;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.ui.module.FrameworkModule;
import org.springframework.context.ApplicationContext;

import com.google.inject.Injector;

/**
 * Command line launcher just to test that I can open a singly linked list repository file.
 * 
 * @author smiley
 * @since 14-04-2020
 */
public class ListLauncher {
	private static final Logger log = Logger.getLogger(ListLauncher.class);
	private final ApplicationContext appContext;
	
	@Inject
	private ListLauncher(ListContextBuilder contextBuilder) {
		appContext = contextBuilder.build();
	}
		
	public void launch() {		
		log.debug("launch() building spring context ...");
		RepositoryFile repo = getBean("repo");
		log.debug("file mode: " + repo.getFileMode().toString());
		
		try {
			log.debug("file size: " + repo.size());
		} catch(IOException e) {
			log.error("i/o exception getting file size", e);
		}
		
		log.debug("file name: " + repo.getFileName());
		
		RepositoryManager repoman = getBean("repoman");
		Sizing sizing = repoman.getSizing();
		log.debug("block size: " + sizing.getBlockSize());
		log.debug("slot size: " + sizing.getSlotSize());
		log.debug("sizing reports slot count in root block: " + sizing.getRootBlockSlotCount());
		log.debug("sizing reports slot count in other blocks: " + sizing.getOtherBlockSlotCount());
		log.debug("page cache size: " + repoman.getPageCacheSize());
		SandpitManager sandpitManager = repoman.getSandpitManager();
		Sandpit sandpit = sandpitManager.getSandpit();
		log.debug("sandpit: " + sandpit.toString());
		log.debug("repository overhead: " + repoman.getRepositoryOverhead().toString());
		
		try {
			StorageManager rootBlock = repoman.getStorageManager(0L);
			
			log.debug("root block ID: " + rootBlock.getBlockId());
			log.debug("root block slot count: " + rootBlock.getSlotCount());

			log.debug("root block type: " + rootBlock.getBlockOverhead().getBlockType().toString());
			log.debug("used slots:");
			
			
			for (Slot slot : rootBlock.getUsedSlots()) {
				log.debug("b" + rootBlock.getBlockId() + "s" + slot.getSlotId() + " locator=" + slot.getLocator() + "L " + slot.getSlotOverhead().toString());
			}
			
			

		} catch (PhysicalResourceException e) {
			log.error("failed to get storage manager", e);
		}
		
		try {
			log.debug("shutting down the repo manager ...");
			repoman.shutdown();
		} catch (PhysicalResourceException e) {
			log.error("shutdown failed", e);
		}
		
		log.info("main thread says goodbye");
	}
	

	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName) {
		final Object object = appContext.getBean(beanName);
		
		if (object == null) {
			log.error("did not find a bean called '" + beanName + "'");
			return null;
		}
		
		return (T) object;
	}

	public static void main(String... args) {
		final int argumentCount = args.length;
		
		if (argumentCount != 1) {
			System.err.println("usage: " + ListLauncher.class.getSimpleName() + " resourceName");
		}
		
		// pass in the name of the nv file from the runner arguments, so we can plug it into the gui framework module
		final String resourceName = args[0];
		Injector injector = createInjector(new FrameworkModule(resourceName), new ListLauncherModule());
		final ListLauncher launcher = injector.getInstance(ListLauncher.class);
		launcher.launch();
	}
}

package org.hydroid.beowulf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.hydroid.beowulf.manager.BeowulfRuntimeManager;
import org.hydroid.beowulf.manager.RuntimeManager;
import org.hydroid.beowulf.manager.SandpitManager;
import org.hydroid.beowulf.manager.SandpitManagerImpl;
import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.manager.StorageManagerImpl;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.RepositoryOverhead;
import org.hydroid.beowulf.overlay.RootBlock;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.StorageBlock;
import org.hydroid.beowulf.overlay.creator.StorageBlockCreator;
import org.hydroid.beowulf.space.SpaceManagementContext;
import org.hydroid.beowulf.space.SpaceManager;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * manages the run-time state of the repository.
 * 
 * @author smiley
 *
 */
public class BeowulfRepositoryManager implements RepositoryManager {
	private static final Logger logger = LoggerFactory.getLogger(RepositoryManager.class);
	


	private final Map<Long, StorageManager> lookUp = new HashMap<Long, StorageManager>();
	private final int blockSize;
	private final List<StorageManager> storageManagers;
	private final OverlayFactory reader;
	private final OverlayFactory forge;
	private final RootBlock rootBlock;
	private final MetaData md;
	private final Sizing sz;
	private final RepositoryFile repoFile;
	private final PageDaemon pageDaemon;
	private final RepositoryOverhead repositoryOverhead;
	private final RuntimeManager runtime;
	private final SandpitManager sandpitManager;
	private final SpaceManager space;
	private final StorageManagerContext storageManagerContext;
	private final LocatorFactory locatorFactory;
	private final SpaceManagementContext spaceManagementContext;

	public BeowulfRepositoryManager(PageDaemon pageDaemon, RepositoryFile repoFile, PageIdentifier rootPageId,
			                        SpaceManagementContext ctx) throws PhysicalResourceException {

		this.pageDaemon = pageDaemon;
		this.repoFile = repoFile;
		this.spaceManagementContext = ctx;
		this.reader = ctx.getReader();
		this.forge = ctx.getForge();
		this.space = ctx.getSpaceManager();
		this.locatorFactory = ctx.getLocatorFactory();
		
		// provide a call-back for the space manager to ask us to forge more space
		space.setForgeCallback(
			new Callable<StorageManager>() {

				@Override
				public StorageManager call() throws Exception {
					return makeStorage();
				}
				
			}
		);
		
		space.setChain(ctx.getChain());
		
		// page in and pin the root block, as it always has to be resident
		Page rootPage = pageDaemon.pageIn(rootPageId);
		pageDaemon.pin(rootPage);
		rootBlock = new RootBlock(rootPage.getByteBuffer(), reader, locatorFactory);
		this.repositoryOverhead = rootBlock.getRepositoryOverhead();
		md = rootBlock.getMetaData();
		sz = rootBlock.getSizing();
		blockSize = sz.getBlockSize();
		
		// create a context to pass to all storage managers
		storageManagerContext = new StorageManagerContext(reader, forge, sz);
		
		storageManagers = new ArrayList<StorageManager>(pageDaemon.getPageLimit());
		FreeSlotList fsl = rootBlock.getFreeSlotList();
		BlockOverhead bo = rootBlock.getBlockOverhead();
		runtime = new BeowulfRuntimeManager(rootBlock.getRepositoryOverhead());
		sandpitManager = new SandpitManagerImpl(rootBlock.getSandpit(), locatorFactory);
		addManager(0L, new StorageManagerImpl(storageManagerContext, 0L, bo, fsl, 
				                              rootBlock.getSlotOverheads(), 
				                              rootBlock.getBuffers(),
				                              locatorFactory), true);		
	}

	public StorageManager getStorageManager(long blockId) throws PhysicalResourceException {
		StorageManager hit = lookUp.get(blockId);
		
		if (hit != null) {
			return hit;
		}
		
		PageIdentifier id = PageIdentifier.forBlock(repoFile, blockId, blockSize);
		Page blockPage = pageDaemon.pageIn(id);
		StorageBlock block = new StorageBlock(blockPage.getByteBuffer(), reader, locatorFactory, sz);
		FreeSlotList fsl = block.getFreeSlotList();
		BlockOverhead bo = block.getBlockOverhead();
		StorageManager storageManager = new StorageManagerImpl(storageManagerContext, blockId, bo, fsl, 
				                                               block.getSlotOverheads(),
				                                               block.getBuffers(),
				                                               locatorFactory);
		addManager(blockId, storageManager, true);
		return storageManager;
	}

	private void addManager(long blockId, StorageManager storage, boolean notify) {
		storageManagers.add(storage);
		lookUp.put(blockId, storage);
		
		if (notify) {
			space.notify(storage);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public StorageManager makeStorage() throws PhysicalResourceException {
		long nextBlockId = runtime.nextBlockId();
		logger.debug(String.format("makeStorage(blockId=%d)", nextBlockId));
		PageIdentifier newPage = PageIdentifier.forBlock(repoFile, nextBlockId, blockSize);
		Page page1 = pageDaemon.make(newPage);
		ByteBuffer b1 = page1.getByteBuffer();
		
		// create a new storage block and peel out a few values
		StorageBlockCreator storageBlock = new StorageBlockCreator(nextBlockId, b1, forge, locatorFactory, sz);
		BlockOverhead bo = storageBlock.getBlockOverhead();
		FreeSlotList fsl = storageBlock.getFreeSlotList();
		long blockId = bo.getBlockId();
		
		StorageManager mgr = new StorageManagerImpl(storageManagerContext, blockId, bo, fsl, 
				                                    storageBlock.getSlotOverheads(),
				                                    storageBlock.getBuffers(),
				                                    locatorFactory);
		addManager(blockId, mgr, false);
		return mgr;
	}	
		
	@Override
	public long getBlockCount() {
		return runtime.getBlockCount();
	}
	
	public MetaData getMetaData() { return md; }
	public Sizing getSizing() { return sz; }
	public SandpitManager getSandpitManager() { return sandpitManager; }
	public LocatorFactory getLocatorFactory() { return locatorFactory; }
	public SpaceManagementContext getSpaceManagementContext() { return spaceManagementContext; }
	public RepositoryOverhead getRepositoryOverhead() { return repositoryOverhead; }
	
	@Override
	public int getPageCacheSize() { return pageDaemon.getPageLimit(); }

	@Override
	public void shutdown() throws PhysicalResourceException {
		pageDaemon.flushAll();
		
		try {
			repoFile.close();
		} catch (IOException e) {
			throw new PhysicalResourceException("could not close repository file", e);		
		}
	}

}

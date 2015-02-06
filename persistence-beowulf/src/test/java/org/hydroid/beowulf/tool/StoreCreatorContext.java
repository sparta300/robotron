package org.hydroid.beowulf.tool;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.daemon.PageDaemon;
import com.lbg.persist.daemon.PageIdentifier;

public class StoreCreatorContext {
	
	public StoreCreatorContext(PageDaemon pageDaemon, RepositoryFile repositoryFile, PageIdentifier pageIdentifier,
			                   OverlayFactory overlayFactory, LocatorFactory locatorFactory, int blockSize, int slotSize) {
		daemon = pageDaemon;
		repo = repositoryFile;
		pageId = pageIdentifier;
		forge = overlayFactory;
		this.locatorFactory = locatorFactory;
		this.blockSize = blockSize;
		this.slotSize = slotSize;
	}
	
	public int getBlockSize() { return blockSize; }
	public int getSlotSize() { return slotSize; }
	public PageIdentifier getPageId() {	return pageId; }
	public PageDaemon getDaemon() { return daemon; }
	public RepositoryFile getRepo() { return repo; }
	public OverlayFactory getForge() { return forge; }
	public LocatorFactory getLocatorFactory() { return locatorFactory; }
	
	private LocatorFactory locatorFactory;
	private PageIdentifier pageId;
	private PageDaemon daemon;
	private RepositoryFile repo;
	private OverlayFactory forge;
	private int blockSize;
	private int slotSize;
}

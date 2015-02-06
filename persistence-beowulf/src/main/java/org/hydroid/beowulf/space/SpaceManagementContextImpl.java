package org.hydroid.beowulf.space;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

public class SpaceManagementContextImpl implements SpaceManagementContext {
	public SpaceManagementContextImpl(SpaceManager spaceManager, ResponsibilityChain chain, LocatorFactory locatorFactory) {
		this.spaceManager = spaceManager;
		this.chain = chain;
		this.locatorFactory = locatorFactory;
		reader = new OverlayFactory(false, locatorFactory);
		forge  = new OverlayFactory(true, locatorFactory);
	}
	
	

	public ResponsibilityChain getChain() {	return chain; }
	public SpaceManager getSpaceManager() {	return spaceManager; }
	public OverlayFactory getForge() { return forge; }
	public LocatorFactory getLocatorFactory() {	return locatorFactory; }
	public OverlayFactory getReader() {	return reader;	}	

	private final LocatorFactory locatorFactory;
	private final OverlayFactory forge;
	private final OverlayFactory reader;
	private final SpaceManager spaceManager;
	private final ResponsibilityChain chain;
}

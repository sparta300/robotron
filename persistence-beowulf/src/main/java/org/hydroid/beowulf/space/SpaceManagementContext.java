package org.hydroid.beowulf.space;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

public interface SpaceManagementContext {
	SpaceManager getSpaceManager();
	ResponsibilityChain getChain();
	OverlayFactory getForge();
	OverlayFactory getReader();
	LocatorFactory getLocatorFactory();
}

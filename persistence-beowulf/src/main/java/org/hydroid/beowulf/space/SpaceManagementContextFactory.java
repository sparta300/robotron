package org.hydroid.beowulf.space;

import org.hydroid.beowulf.storage.LocatorFactory;


public interface SpaceManagementContextFactory {
	SpaceManagementContext make();

	LocatorFactory getLocatorFactory();
}

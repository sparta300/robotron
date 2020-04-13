package org.hydroid.beowulf;

import org.hydroid.beowulf.space.SpaceManagementContextFactory;

import org.hydroid.page.PageDaemon;

public interface InstallationManagerApi {

	String getHostName();
	
	String getSuffix();

	SpaceManagementContextFactory getSpaceManagementContextFactory();

	PageDaemon getPageDaemon();

}

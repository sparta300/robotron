package org.hydroid.beowulf;

import org.hydroid.beowulf.space.SpaceManagementContextFactory;

import com.lbg.persist.daemon.PageDaemon;

public interface InstallationManagerApi {

	String getHostName();
	
	String getSuffix();

	SpaceManagementContextFactory getSpaceManagementContextFactory();

	PageDaemon getPageDaemon();

}

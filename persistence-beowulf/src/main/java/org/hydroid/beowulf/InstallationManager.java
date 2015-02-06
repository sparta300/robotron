package org.hydroid.beowulf;

import org.hydroid.beowulf.space.SpaceManagementContextFactory;

import com.lbg.persist.daemon.PageDaemon;

public class InstallationManager implements InstallationManagerApi {
	public InstallationManager(String hostName, PageDaemon pageDaemon, SpaceManagementContextFactory spaceManagementContextFactory,
			                   String suffix) {
		this.hostName = hostName;
		this.pageDaemon = pageDaemon;
		this.spaceManagementContextFactory = spaceManagementContextFactory;
		this.suffix = suffix;
	}


	public String getSuffix() { return suffix; }
	public String getHostName() { return hostName; }
	public PageDaemon getPageDaemon() { return pageDaemon; }
	public SpaceManagementContextFactory getSpaceManagementContextFactory() { return spaceManagementContextFactory; }

	private final SpaceManagementContextFactory spaceManagementContextFactory;
	private final PageDaemon pageDaemon;
	private final String hostName;
	private final String suffix;
}

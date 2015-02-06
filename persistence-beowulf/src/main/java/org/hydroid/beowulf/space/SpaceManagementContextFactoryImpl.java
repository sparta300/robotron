package org.hydroid.beowulf.space;

import java.util.Arrays;

import org.hydroid.beowulf.storage.LocatorFactory;

public class SpaceManagementContextFactoryImpl implements SpaceManagementContextFactory {
	public SpaceManagementContextFactoryImpl(LocatorFactory locatorFactory) {
		this.locatorFactory = locatorFactory;
	}
	
	@Override
	public SpaceManagementContext make() { 
		SpaceManager spaceManager = new BeowulfSpaceManager();
		
		Scavenger scavenger = new Scavenger();
		
		NewSpaceHandler creator = new NewSpaceHandler(spaceManager, 1, scavenger);

		ExistingSpaceHandler sniffer = new ExistingSpaceHandler(spaceManager);
		
		ResponsibilityChain chain = new ResponsibilityChain(Arrays.asList(sniffer, scavenger, creator));
		
		return new SpaceManagementContextImpl(spaceManager, chain, locatorFactory);
	}
	
	public LocatorFactory getLocatorFactory() { return locatorFactory; }
	

	private LocatorFactory locatorFactory;
}

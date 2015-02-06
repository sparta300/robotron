package org.hydroid.beowulf.storage.general;

import org.hydroid.beowulf.space.ResponsibilityChain;
import org.hydroid.beowulf.storage.LocatorFactory;

public class ApiContext {
	public ApiContext(ResponsibilityChain chain, LocatorFactory locatorFactory) {
		this.chain = chain;
		this.locatorFactory = locatorFactory;
	}

	public ResponsibilityChain getChain() {	return chain; }
	public LocatorFactory getLocatorFactory() { return locatorFactory; }

	private LocatorFactory locatorFactory;
	private ResponsibilityChain chain;
}

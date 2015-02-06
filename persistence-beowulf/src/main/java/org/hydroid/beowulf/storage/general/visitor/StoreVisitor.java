package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.space.SpaceRequest;
import org.hydroid.beowulf.storage.LocatorFactory;

public class StoreVisitor extends AddVisitor {
	public StoreVisitor(byte[] data, SpaceRequest request, LocatorFactory locatorFactory) {
		super(data, request, locatorFactory);
	}
}

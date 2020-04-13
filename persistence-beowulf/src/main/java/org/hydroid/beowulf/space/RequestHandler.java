package org.hydroid.beowulf.space;

import org.hydroid.file.PhysicalResourceException;

public interface RequestHandler {
	void handleRequest(SpaceRequest request) throws PhysicalResourceException;

	void setSuccessor(RequestHandler successor);
	
	RequestHandler getSuccessor();
}

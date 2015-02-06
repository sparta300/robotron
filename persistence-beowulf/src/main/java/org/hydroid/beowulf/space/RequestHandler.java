package org.hydroid.beowulf.space;

import com.lbg.resource.PhysicalResourceException;

public interface RequestHandler {
	void handleRequest(SpaceRequest request) throws PhysicalResourceException;

	void setSuccessor(RequestHandler successor);
	
	RequestHandler getSuccessor();
}

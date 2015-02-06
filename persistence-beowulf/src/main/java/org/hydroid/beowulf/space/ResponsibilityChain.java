package org.hydroid.beowulf.space;

import java.util.List;

import com.lbg.resource.PhysicalResourceException;

/**
 * space management using a chain of responsibility pattern.
 * 
 * @author smiley
 *
 */
public class ResponsibilityChain implements RequestHandler {
	public ResponsibilityChain(List<RequestHandler> handlerChain) {
		if (handlerChain == null) {
			head = null;
			return;
		}

		int handlerCount = handlerChain.size();
		head = handlerChain.get(0);
		head.setSuccessor(null);
		
		if (handlerCount == 1) {
			return;
		}

		int nextIndex = 1;
		last = head;
		RequestHandler nextHandler = null;

		for (RequestHandler handler : handlerChain) {
			if (nextIndex > (handlerCount - 1)) {
				nextHandler = null;
			} else {
				nextHandler = handlerChain.get(nextIndex);
			}

			handler.setSuccessor(nextHandler);
			last = handler;
			nextIndex++;
		}
	}

	public ResponsibilityChain() {
		head = null;
	}

	public ResponsibilityChain(RequestHandler head) {
		setHead(head);
	}

	public void setSuccessor(RequestHandler successor) {
		if (last == null) {
			if (head == null) {
				setHead(successor);
				return;
			}
		}

		last.setSuccessor(successor);
		last = successor;
	}

	private void setHead(RequestHandler head) {
		this.head = head;
		head.setSuccessor(null);
	}

	public void handleRequest(SpaceRequest request)
			throws PhysicalResourceException {
		if (head == null) {
			throw new PhysicalResourceException("chain of responsibility does not have any handlers defined");
		}

		head.handleRequest(request);
	}
	
	public RequestHandler getSuccessor() { return head; }

	private RequestHandler head;
	private RequestHandler last;
}

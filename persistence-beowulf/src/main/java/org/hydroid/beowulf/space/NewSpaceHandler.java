package org.hydroid.beowulf.space;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;

import com.lbg.resource.PhysicalResourceException;

public class NewSpaceHandler implements RequestHandler {
	public NewSpaceHandler(SpaceManager spaceManager, int incrementMinimumBlocks, StorageListener scavenger) {
		spaceman = spaceManager;
		chunkSize = incrementMinimumBlocks;
		this.scavenger = scavenger;
	}

	@Override
	public void handleRequest(SpaceRequest request)	throws PhysicalResourceException {
		Sizing sz = request.getSizing();
		int requiredSlotCount = request.getRemainingSlotCount();
		int perBlock = sz.getOtherBlockSlotCount();
		int blockCount = chunkSize;
		
		if (requiredSlotCount > blockCount) {
			blockCount = requiredSlotCount / perBlock;
		
			if (requiredSlotCount > (blockCount * perBlock)) {
				blockCount++;
			}
		}
		
		List<Slot> createdSlots = new ArrayList<Slot>(requiredSlotCount);
		
		for (int b = 0; b < blockCount; b++) {
			createdSlots.addAll(spaceman.forge());
		}
		
		// keep adding slots until the request is satisfied
		for (int s = 0; s < requiredSlotCount; s++) {			
			if (!request.isSatisfied()) {
				Slot slot = createdSlots.remove(0);
				logger.debug(String.format("respond(b%ds%d)", slot.getBlockId(), slot.getSlotId()));
				request.respond(slot);
			}
		}

		// leave any surplus slots for the scavenger
		for (Slot slot : createdSlots) {
			scavenger.offer(slot);
		}
	}


	@Override
	public void setSuccessor(RequestHandler successor) {
        this.successor = successor;
	}
	
	public RequestHandler getSuccessor() {
		return successor;
	}

	private static final Logger logger = LoggerFactory.getLogger(NewSpaceHandler.class);

	private SpaceManager spaceman;
	private StorageListener scavenger;
	private int chunkSize;
	private RequestHandler successor;
}

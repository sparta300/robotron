package org.hydroid.beowulf.space;

import java.util.ArrayList;
import java.util.List;

import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.resource.OutOfSpace;


/**
 * space request handler that tries to satisfy the request using free slots already in the store.
 * 
 * @author smiley
 *
 */
public class ExistingSpaceHandler implements RequestHandler, StorageListener
{
	public ExistingSpaceHandler(SpaceManager spaceManager) {
		spaceManager.register(this);
	}

    public void handleRequest(SpaceRequest request) throws PhysicalResourceException
    {
        int freeSlotCount = this.freeSlots.size();
        
        // look at how many slots are left rather than how many were originally requested.  There may be other
        // space request strategies and acceptors which can service this request
        int requiredSlotCount = request.getRemainingSlotCount();
        
        // if we can completely satisfy the request, then respond with each of the free slots
        if (freeSlotCount >= requiredSlotCount)
        {
            logger.debug("can completely satisfy request *free=" + freeSlotCount + " >= *required=" + requiredSlotCount);
            respond(request, requiredSlotCount);
        }
        else if (freeSlotCount > 0)
        {
            // otherwise just respond with as many slots as we have available
            logger.debug("can partially satisfy request *free=" + freeSlotCount + " < *required=" + requiredSlotCount);
            respond(request, freeSlotCount);
            ensureSuccessor(request);
            successor.handleRequest(request);            
        }        
        else
        {
            logger.debug("cannot satisfy request, no free slots *required=" + requiredSlotCount);
            ensureSuccessor(request);            
            successor.handleRequest(request);
        }
    }

    private void ensureSuccessor(SpaceRequest request) throws OutOfSpace
    {
        if (successor == null)
        {
            throw new OutOfSpace(request.getTransactionId(), request.getRemainingSlotCount());
        }
    }

    private void respond(SpaceRequest request, int responseSlotCount)
    {
        for (int s = 0; s < responseSlotCount; s++)
        {
        	Slot slot = freeSlots.remove(0);
    		logger.debug(String.format("respond(b%ds%d)", slot.getBlockId(), slot.getSlotId()));
    		request.respond(slot);        	
        }
    }
    
    public void setSuccessor(RequestHandler successor)
    {
        this.successor = successor;
    }
    
	public RequestHandler getSuccessor() {
		return successor;
	}    

    /**
     * we get notified when blocks are loaded, so that we can use any space they may have available
     * @param rootBlock
     */
    public boolean offer(Slot slot) {
    	freeSlots.add(slot);
    	logger.debug(String.format("accepting b%ds%d", slot.getBlockId(), slot.getSlotId()));
    	return true;
    }

    private List<Slot> freeSlots = new ArrayList<Slot>();    
    private RequestHandler successor;
    
    private Logger logger = LoggerFactory.getLogger(ExistingSpaceHandler.class);
}

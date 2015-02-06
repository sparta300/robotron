/**
 * File		: $Source:$
 * Author	: $Author:$
 * Revision	: $Revision:$
 * Date 	: $Date:$
 */
package org.hydroid.beowulf.space;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.general.AddObjectRequest;

/**
 * represents any request for slot space.
 * 
 * @author smiley
 */
public class SpaceRequestImpl implements SpaceRequest {
	public SpaceRequestImpl(AddObjectRequest request, int spaceRequestId, Sizing sizing) {
		this.transactionId = request.getTransactionId();
		this.remainingByteCount = request.getByteCount();
		this.id = spaceRequestId;
		this.sizing = sizing;

		performCalculations();
	}

	private void performCalculations() {
		this.slotSize = sizing.getSlotSize();
		this.timeStamp = System.currentTimeMillis();

		int slotCount = remainingByteCount / sizing.getSlotSize();
		int remaining = remainingByteCount % sizing.getSlotSize();
		logger.debug("*bytes=" + remainingByteCount + " *slots=" + slotCount + " *remaining=" + remaining);

		if (remaining != 0) {
			slotCount++;
			logger.debug("remaining=" + remaining + " revised *slots=" + slotCount);
		}

		this.remainingSlotCount = slotCount;
		this.requestedByteCount = remainingByteCount;
		this.requestedSlotCount = this.remainingSlotCount;
	}

	public void respond(Slot freeSlot) {
		response.add(freeSlot);
		remainingSlotCount--;

		if (remainingByteCount > slotSize) {
			remainingByteCount -= slotSize;
		} else {
			remainingByteCount = 0;
		}
	}

	void respond(Slot[] freeSlots) {
		int freeSlotCount = freeSlots.length;

		for (int s = 0; s < freeSlotCount; s++) {
			respond(freeSlots[s]);
		}
	}

	public boolean isSatisfied() {
		return remainingSlotCount == 0;
	}
	
    public List<Slot> getResponse() { return response; }
    
    public void setLocator(long locator) { this.locator = locator; }
    
    public int getRemainingSlotCount(){ return remainingSlotCount; }
    
    public int getTransactionId(){ return transactionId; }
    
    int getId(){ return id; }
    
    int getRemainingByteCount(){ return remainingByteCount; }
    int getRequestedSlotCount() { return requestedSlotCount; }
    int getRequestedByteCount() { return requestedByteCount; }
    long getTimeStamp() { return timeStamp; }
    public Sizing getSizing() { return sizing; }
    
    public long getLocator(){ return locator; }

	private long locator;
	private Sizing sizing;
	private int slotSize;
	private int remainingSlotCount;
	private int remainingByteCount;
	private int requestedSlotCount;
	private int requestedByteCount;
	private int id;
	private int transactionId;
	private List<Slot> response = new ArrayList<Slot>();
	private long timeStamp;

	private static final Logger logger = LoggerFactory.getLogger(SpaceRequestImpl.class);
}

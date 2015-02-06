package org.hydroid.beowulf.space;

import java.util.ArrayList;
import java.util.List;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;

public class SlotRequest implements SpaceRequest {
	public SlotRequest(int spaceRequestId, int transactionId, Sizing sz) {
		this(spaceRequestId, transactionId, sz, 1);
	}
	
	public SlotRequest(int spaceRequestId, int transactionId, Sizing sz, int requiredSlotCount) {
		super();
		this.spaceRequestId = spaceRequestId;
		this.transactionId = transactionId;
		this.sz = sz;
		this.remainingSlotCount = requiredSlotCount;
	}

	public boolean isSatisfied() {
		return remainingSlotCount == 0;
	}
	
	public void respond(Slot freeSlot) {
		response.add(freeSlot);
		remainingSlotCount--;
	}	

	private int remainingSlotCount;
	
	private List<Slot> response = new ArrayList<Slot>();
	
    public List<Slot> getResponse() { return response; }	
	public int getRemainingSlotCount() { return remainingSlotCount;	}
	public Sizing getSizing() { return sz; }
	public int getTransactionId() { return transactionId; }
	public int getSpaceRequestId() { return spaceRequestId; }		

	private int spaceRequestId;
	private int transactionId;
	private Sizing sz;
}

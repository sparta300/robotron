package org.hydroid.beowulf.overlay.creator;


public class SlotCountCalculator {
	public SlotCountCalculator(int blockSize, int slotSize, int preambleSize, int blockOverheadSize, int slotOverheadSize) {
		int rootSpace = blockSize - preambleSize;
		int otherSpace = blockSize - blockOverheadSize;
		
		// we have opne byte for each slot in the free list
		int perSlotSpace = slotOverheadSize + slotSize + 1;
		
		rootBlockSlotCount = rootSpace / perSlotSpace;
		otherBlockSlotCount = otherSpace / perSlotSpace;
	}
	
	public int getRootBlockSlotCount() { return rootBlockSlotCount;	}
	public int getOtherBlockSlotCount() { return otherBlockSlotCount; }

	private final int rootBlockSlotCount;
	private final int otherBlockSlotCount;
}


package org.hydroid.beowulf.storage;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.SlotOverhead;

/**
 * an implementation of the {@link Slot} interface.
 * 
 * @author smiley
 *
 */
public class SlotImpl implements Slot {
	public SlotImpl(StorageManager storageManager, long blockId, int slotId, 
			        ByteBuffer byteBuffer, 
			        BlockOverhead blockOverhead,
			        FreeSlotList freeSlotList,
			        SlotOverhead slotOverhead) {
		this.storageManager = storageManager;
		this.blockId = blockId;
		this.slotId = slotId;
		this.locator = slotOverhead.getLocator();
		bb = byteBuffer;
		bo = blockOverhead;
		fsl = freeSlotList;
		so = slotOverhead;
	}	
	
	public String toString() {
		return String.format("b%ds%d", blockId, slotId);
	}

	public long getBlockId() { return blockId; }
	public int getSlotId() { return slotId; }	
	public long getLocator() { return locator;	}
	public ByteBuffer getBuffer() { return bb; }
	public SlotOverhead getSlotOverhead() { return so; }
	public BlockOverhead getBlockOverhead() { return bo; }
	public FreeSlotList getFreeSlotList() { return fsl; }
	public StorageManager getStorageManager() { return storageManager; }
	
	private StorageManager storageManager;
	private FreeSlotList fsl;
	private BlockOverhead bo;
	private SlotOverhead so;
	private long blockId;
	private int slotId;
	private long locator;
	private ByteBuffer bb;
}

package org.hydroid.beowulf.manager;

import java.nio.ByteBuffer;
import java.util.List;

import org.hydroid.beowulf.StorageManagerContext;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.BufferReference;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.Slot;

public interface StorageManager {
	int getSlotCount();
	List<Slot> getUsedSlots();
	FreeSlotList getFreeSlotList();
	FreeSlotManager getFreeSlotManager();
	SlotOverhead getSlotOverhead(int index);
	BlockOverhead getBlockOverhead();
	long getBlockId();
	BufferReference[] getBuffers();
	ByteBuffer getBuffer(int slotId);
	List<Slot> drainFreeSlots();	
	StorageManagerContext getContext();
	void allocateSlot(int slotIndex, int size, int typeId);
	Slot getSlotAt(int slotId);
}

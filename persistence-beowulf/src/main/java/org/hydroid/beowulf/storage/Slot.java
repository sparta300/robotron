package org.hydroid.beowulf.storage;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.SlotOverhead;

public interface Slot
{
    int getSlotId();
    
    SlotOverhead getSlotOverhead();
    
    long getLocator();
    
    ByteBuffer getBuffer();
    
    BlockOverhead getBlockOverhead();
    
    long getBlockId();

	FreeSlotList getFreeSlotList();
	
	StorageManager getStorageManager();
}

package org.hydroid.beowulf.manager;

import org.hydroid.beowulf.overlay.FreeListRuntime;

public interface FreeList256Manager {

	void deallocateSlot(int subslotIndex);

	void allocateSlot(int freeSlotIndex);

	int findFreeSlot();

	FreeSlotManager getFreeSlotManager();
	
	FreeListRuntime getFreeListRuntime();
}

package org.hydroid.beowulf.manager;

public interface FreeSlotManager {
	int[] findFreeSlots();

	int findFreeSlot();

	void allocateSlot(int subslotIndex);

	void deallocateSlot(int subslotIndex);

	int[] findUsedSlots();

	int[] getSlots();
}

package org.hydroid.beowulf.manager;

import static org.hydroid.beowulf.BeowulfConstants.EMPTY_INT_ARRAY;
import static org.hydroid.beowulf.BeowulfConstants.NO_FREE_SLOT;
import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;

import org.hydroid.beowulf.overlay.FreeSlotList;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.UnsignedBytePointer;

/**
 * manages the free slot list and the block overhead.
 * 
 * @author smiley
 */
public class FreeSlotManagerImpl implements FreeSlotManager {
	public FreeSlotManagerImpl(FreeSlotList freeSlotList) {
		fsl = freeSlotList;
		slotCount = freeSlotList.getSlotCount();
		pointers = fsl.getPointers();
	}

	/**
	 * counts the number of free slots remaining in the free list
	 * 
	 * @return
	 */
	public int countFreeSlots() {
		int free = 0;

		for (int p = 0; p < this.slotCount; p++) {
			if (pointers[p].get() != SLOT_USED) {
				free++;
			}
		}

		return free;
	}

	/**
	 * @return the first free slot that can be found or <code>NO_FREE_SLOT</code> if there aren't any
	 */
	public int findFreeSlot() {
		for (int p = 0; p < this.slotCount; p++) {
			if (pointers[p].get() != SLOT_USED) {
				return p;
			}
		}

		return NO_FREE_SLOT;
	}

	/**
	 * finds all available free slots.
	 * 
	 * @return an array of slot indexes or any empty array if no slots are free
	 */
	public int[] findFreeSlots() {
		int[] array = new int[this.slotCount];
		int index = 0;

		for (int p = 0; p < this.slotCount; p++) {
			if (pointers[p].get() != SLOT_USED) {
				array[index++] = p;
			}
		}

		if (index == 0) {
			return EMPTY_INT_ARRAY;
		}

		int size = index;
		int[] arrayToReturn = new int[size];

		for (int p = 0; p < size; p++) {
			arrayToReturn[p] = array[p];
		}

		return arrayToReturn;
	}
	
	public int[] findUsedSlots() {
		return findSlots(FreeSlotSearchType.USED);
	}
	
	public int[] getSlots() {
		return findSlots(FreeSlotSearchType.TOTAL);
	}	
	
	private int[] findSlots(FreeSlotSearchType searchType) {
		int[] array = new int[this.slotCount];
		int index = 0;

		for (int p = 0; p < this.slotCount; p++) {
			if (searchType.select(pointers[p].get())) {
				array[index++] = p;
			}
		}

		if (index == 0) {
			return EMPTY_INT_ARRAY;
		}

		int size = index;
		int[] arrayToReturn = new int[size];

		for (int p = 0; p < size; p++) {
			arrayToReturn[p] = array[p];
		}

		return arrayToReturn;
	}	

	public void allocateSlot(int index) {
		pointers[index].set(SLOT_USED);
	}
	
	public void deallocateSlot(int index) {
		pointers[index].set(SafeCast.fromIntToUnsignedByte(index));
	}

	public void stringify(StringBuilder buf) {
		buf.append(EOL);

		for (int p = 0; p < this.slotCount; p++) {

			if (pointers[p].get() == SLOT_USED) {
				buf.append("[" + p + "] in use");
				buf.append(EOL);
			} else {
				buf.append("[" + p + "] free");
				buf.append(EOL);
			}
		}
	}

	public String toString() {
		StringBuilder buf = new StringBuilder();
		stringify(buf);
		return buf.toString();
	}
	
	private static final String EOL = System.getProperty("line.separator");
	
	private final UnsignedBytePointer[] pointers;
	private int slotCount;
	private FreeSlotList fsl;
}

package org.hydroid.beowulf.storage.general;

import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;

/**
 * an interface for locating slots by locator or block/slot IDs.
 * 
 * @author smiley
 *
 */
public interface SlotFinder {
	Slot find(long locator) throws PhysicalResourceException ;
	Slot find(long blockId, int slotId) throws PhysicalResourceException ;
}

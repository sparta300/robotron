package org.hydroid.beowulf.storage.general;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.RepositoryManager;
import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotImpl;
import org.hydroid.file.PhysicalResourceException;

/**
 * a simple interface for finding slots by locator or block/slot ID.
 * 
 * @author smiley
 *
 */
public class SlotFinderImpl implements SlotFinder {
	public SlotFinderImpl(RepositoryManager repositoryManager) {
		repoman = repositoryManager;
		locatorFactory = repositoryManager.getLocatorFactory();
	}

	public Slot find(long locator) throws PhysicalResourceException  {
		Locator mask = locatorFactory.make(locator);
		return find(mask.getBlockId(), mask.getStructureId());
	}

	public Slot find(long blockId, int slotId) throws PhysicalResourceException {
		StorageManager manager = repoman.getStorageManager(blockId);
		SlotOverhead so = manager.getSlotOverhead(slotId);
		BlockOverhead bo = manager.getBlockOverhead();
		FreeSlotList fsl = manager.getFreeSlotList();
		ByteBuffer bb = manager.getBuffer(slotId);
	
		return new SlotImpl(manager, blockId, slotId, bb, bo, fsl, so);
	}

	private LocatorFactory locatorFactory;
	private RepositoryManager repoman;
}

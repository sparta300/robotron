package org.hydroid.beowulf.manager;

import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hydroid.beowulf.SlotOverlayType;
import org.hydroid.beowulf.StorageManagerContext;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.BufferReference;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotImpl;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * an implementation of a general storage unit manager.
 * 
 * @author smiley
 */
public class StorageManagerImpl implements StorageManager {
	public StorageManagerImpl(StorageManagerContext context, long blockId, BlockOverhead blockOverhead,
					          FreeSlotList freeSlotList, 
			                  SlotOverhead[] slotOverheads, 
			                  BufferReference[] buffers,
			                  LocatorFactory locatorFactory) {
		this.context = context;
		this.blockId = blockId;
		this.locatorFactory = locatorFactory;
		bo = blockOverhead;
		slotSize = context.getSizing().getSlotSize();
		fsl = freeSlotList;
		fsm = new FreeSlotManagerImpl(fsl);
		this.slotOverheads = slotOverheads;
		this.buffers = buffers;
	}
	
	
	public Slot getSlot(long locator) throws PhysicalResourceException {
		Locator address = locatorFactory.make(locator);
		long actual = address.getBlockId();
		
		if (actual != blockId) {
			throw new PhysicalResourceException(String.format("block ID mismatch: expected %d but got %d", blockId, actual));
		}
		
		if (address.getIndex() != 0) {
			throw new PhysicalResourceException("don't know how to do subslots yet");
		}
		
		return getSlotAt(address.getStructureId());
	}

	@Override
	public Slot getSlotAt(int slotId) {		
		return new SlotImpl(this, blockId, slotId, buffers[slotId].dereference(), bo, fsl, slotOverheads[slotId]);
	}
	
	@Override
	public int getSlotCount() {
		return bo.getTotal();
	}
	
	@Override
	public List<Slot> getUsedSlots() {
		int[] usedSlotIds = fsm.findUsedSlots();
		int usedSlotCount = usedSlotIds.length;
		
		if (usedSlotCount == 0) {
			return Collections.emptyList();
		}
		
		List<Slot> usedSlots = new ArrayList<Slot>(usedSlotCount);
		
		for (int slotId : usedSlotIds) {
			usedSlots.add(getSlotAt(slotId));
		}
			
		return usedSlots;
	}
	
	public List<Slot> drainFreeSlots() {
        int[] freeSlotIds = fsm.findFreeSlots();
        int freeSlotCount = freeSlotIds.length;
                
        // convert slot IDs into slots
        List<Slot> freeSlots = new ArrayList<Slot>(freeSlotCount);
        
        for (int s = 0; s < freeSlotCount; s++) {
        	SlotOverhead so = slotOverheads[s];
        	ByteBuffer bb = buffers[s].dereference();
        	Slot slot = new SlotImpl(this, bo.getBlockId(), freeSlotIds[s], bb, bo, fsl, so);
        	freeSlots.add(slot);
        }
        
        return freeSlots;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void allocateSlot(int slotIndex, int size, int slotOverlayType) {
		logger.debug(String.format("allocate(b%ds%d) size=%d overlay=%s", 
				                    bo.getBlockId(), slotIndex, size, SlotOverlayType.toString(slotOverlayType)));
		
		SlotOverhead so = getSlotOverhead(slotIndex);
		long locator = locatorFactory.make(blockId, slotIndex).asLong();
		so.setLocator(locator);
		so.setTotalRawSize(size);
		so.setTotalStoredSize(size);
		so.setSlotOverlayType(slotOverlayType);
		SlotMask mask = new SlotMask(so.getMaskPointer());
		mask.setHasOverlay(true);
		
		// keep the runtime up to date
        bo.setFree(bo.getFree() - 1);
        bo.setUsed(bo.getUsed() + 1);
        fsl.getPointer(slotIndex).set(SLOT_USED);
	}	

	public byte[] read(Slot headSlot) throws PhysicalResourceException {
		// create a new byte array to contain the deserialised object
		byte[] data = new byte[headSlot.getSlotOverhead().getTotalRawSize()];
		int remaining = data.length;
		int offset = 0;

		// easy case - the deserialised object is not fragmented and is smaller
		// than a slot
		if (remaining <= slotSize) {
			headSlot.getBuffer().position(0).limit(remaining);
			headSlot.getBuffer().get(data, offset, remaining);
			return data;
		}

		// otherwise read in the first slot of the fragmented object
		headSlot.getBuffer().position(0).limit(slotSize);
		headSlot.getBuffer().get(data, offset, slotSize);
		Slot slot = headSlot;

		// read in all the other slots
		while (!SlotMask.isLastFragment(slot.getSlotOverhead().getMask())) {
			// keep the counters up to date
			remaining -= slotSize;
			offset += slotSize;

			// get the next slot
			slot = getSlot(slot.getSlotOverhead().getNextLocator());

			// gather up the data
			if (remaining > slotSize) {
				slot.getBuffer().position(0).limit(slotSize);
				slot.getBuffer().get(data, offset, slotSize);
			} else {
				slot.getBuffer().position(0).limit(remaining);
				slot.getBuffer().get(data, offset, remaining);
			}

		}

		return data;
	}
	
	public SlotOverhead getSlotOverhead(int index) { return slotOverheads[index]; }	
	public ByteBuffer getBuffer(int index) { return buffers[index].dereference(); }

	public StorageManagerContext getContext() { return context; }
	public long getBlockId() { return blockId; }
	public FreeSlotList getFreeSlotList() { return fsl; }
	public BufferReference[] getBuffers() { return buffers; }
	public SlotOverhead[] getSlotOverhads() { return slotOverheads; }
	public BlockOverhead getBlockOverhead() { return bo; }
	public FreeSlotManager getFreeSlotManager() { return fsm; }

	private StorageManagerContext context;
	private FreeSlotManager fsm;
	private BlockOverhead bo;
	private SlotOverhead[] slotOverheads;
	private BufferReference[] buffers;	
	private long blockId;
	private int slotSize;
	private FreeSlotList fsl;
	private LocatorFactory locatorFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(StorageManagerImpl.class);

}

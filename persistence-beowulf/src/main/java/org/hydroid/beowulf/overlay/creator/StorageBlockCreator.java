package org.hydroid.beowulf.overlay.creator;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.BufferReference;
import org.hydroid.beowulf.overlay.CompositeOverlay;
import org.hydroid.beowulf.overlay.EagerBufferReference;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.persist.SafeCast;

public class StorageBlockCreator extends CompositeOverlay {
	public StorageBlockCreator(long blockId, ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory, Sizing sz) {
		super(bb, overlayTypes, factory, locatorFactory);
		bo = getComponent("bo");
		bo.setBlockId(blockId);
		int slotCount = sz.getOtherBlockSlotCount();
		bo.setFree(slotCount);
		bo.setUsed(0);
		bo.setTotal(slotCount);
		
		// back the position up so that we can re-read the 'total' part of the block overhead
		bo.end();
		bb.position(bb.position() - 1);
		
		// now add the free slot list
		int freeSlotListIndex = add(bb, "fsl", factory);
		fsl = getComponent(freeSlotListIndex);
		
		// now add the slots
		slotOverheads = new SlotOverhead[slotCount];
		buffers = new BufferReference[slotCount];
		
		for (int s = 0; s < slotCount; s++) {
			int index = add(bb, "so", factory);
			SlotOverhead slotOverhead = getComponent(index);
			slotOverhead.setSlotIndex(SafeCast.fromIntToUnsignedByte(s));
			slotOverhead.setPosition(slotOverhead.getStart());
			slotOverheads[s] = slotOverhead;
		}
		
		int slotSize = sz.getSlotSize();
		
		for (int b = 0; b < slotCount; b++) {
			BufferReference buffer = new EagerBufferReference(b, bb, slotSize);
			buffers[b] = buffer;
			
			// move along a slot length
			bb.position(bb.position() + slotSize);
		}
		
		markEnd();		
	}

	public BlockOverhead getBlockOverhead() { return bo; }
	public FreeSlotList getFreeSlotList() { return fsl; }
	public BufferReference[] getBuffers() { return buffers; }
	public SlotOverhead[] getSlotOverheads() { return slotOverheads; }
	
	private BufferReference[] buffers;
	private SlotOverhead[] slotOverheads;
	private FreeSlotList fsl;
	
	private BlockOverhead bo;
	private static final List<String> overlayTypes = Arrays.asList("bo");
}

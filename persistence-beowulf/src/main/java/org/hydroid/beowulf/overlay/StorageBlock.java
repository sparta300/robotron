package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.storage.LocatorFactory;

/**
 * a general purpose storage block.
 * 
 * @author C006011
 */
public class StorageBlock extends CompositeOverlay
{
	private static final List<String> overlayTypes = Arrays.asList("bo");

	private final SlotOverhead[] slotOverheads;
	private final BufferReference[] buffers;

	private final FreeSlotList fsl;
	private final long blockId;
	private final BlockOverhead bo;

	public StorageBlock(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory, Sizing sz)
	{
		super(bb, overlayTypes, factory, locatorFactory);
		bo = getComponent("bo");

		// back the position up so that we can re-read the 'total' part of the
		// block overhead
		bo.end();
		bb.position(bb.position() - 1);

		// now add the free slot list
		int freeSlotListIndex = add(bb, "fsl", factory);
		fsl = getComponent(freeSlotListIndex);

		// now add the slots
		int slotCount = bo.getTotal();
		slotOverheads = new SlotOverhead[slotCount];
		buffers = new BufferReference[slotCount];

		for (int s = 0; s < slotCount; s++)
		{
			int index = add(bb, "so", factory);
			SlotOverhead slotOverhead = getComponent(index);
			slotOverheads[s] = slotOverhead;
		}

		int slotSize = sz.getSlotSize();

		for (int b = 0; b < slotCount; b++)
		{
			BufferReference buffer = new EagerBufferReference(b, bb, slotSize);
			buffers[b] = buffer;

			// move along a slot length
			bb.position(bb.position() + slotSize);
		}

		markEnd();

		blockId = bo.getBlockId();
	}

	public FreeSlotList getFreeSlotList()
	{
		return fsl;
	}

	public long getBlockId()
	{
		return blockId;
	}

	public SlotOverhead[] getSlotOverheads()
	{
		return slotOverheads;
	}

	public BufferReference[] getBuffers()
	{
		return buffers;
	}

	public BlockOverhead getBlockOverhead()
	{
		return bo;
	}
}

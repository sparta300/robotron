package org.hydroid.beowulf.overlay.creator;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.SlotCountCalculator;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.CompositeOverlay;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.RepositoryOverhead;
import org.hydroid.beowulf.overlay.RootOverlay;
import org.hydroid.beowulf.overlay.Sandpit;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.persist.SafeCast;

/**
 * 
 * @author C006011
 */
public class RootBlockCreator extends CompositeOverlay implements RootOverlay
{
	private static final List<String> overlayTypes = Arrays.asList("md", "sz", "ro", "sp", "bo");
	
	private final MetaData md;
	private final Sizing sz;
	private final RepositoryOverhead ro;
	private final Sandpit sandpit;
	private final BlockOverhead bo;
	private final FreeSlotList fsl;
	private final SlotOverhead[] slotOverheads;
	
	public RootBlockCreator(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory, int blockSize, int slotSize)
	{
		super(bb, overlayTypes, factory, locatorFactory);
		md = getComponent("md");
		sz = getComponent("sz");
		ro = getComponent("ro");
		sandpit = getComponent("sp");
		bo = getComponent("bo");

		// create a slot overhead just to see how big it is
		bb.mark();
		SlotOverhead so = factory.make("so", bb);
		int slotOverheadSize = so.size();
		bb.reset();

		// calculate the number of slots in the root block and in general
		// storage blocks
		SlotCountCalculator calculator = new SlotCountCalculator(blockSize, slotSize, size(), bo.size(), slotOverheadSize);
		int rootBlockSlotCount = calculator.getRootBlockSlotCount();
		int otherBlockSlotCount = calculator.getOtherBlockSlotCount();

		// now we have everything we need to write the sizing data
		sz.setBlockSize(blockSize);
		sz.setSlotSize(slotSize);
		sz.setSlotOverheadSize(slotOverheadSize);
		sz.setFreeSlotPointerSize(1);
		sz.setRepositoryOverheadSize(ro.size());
		sz.setBlockOverheadSize(bo.size());
		sz.setRootBlockSlotCount(rootBlockSlotCount);
		sz.setOtherBlockSlotCount(otherBlockSlotCount);

		// write out the block overhead data
		bo.setBlockId(0L);
		bo.setTotal(calculator.getRootBlockSlotCount());
		bo.setFree(calculator.getRootBlockSlotCount());
		bo.setUsed(0);

		// back the position up so that we can re-read the 'total' part of the
		// block overhead
		bo.end();
		bb.position(bb.position() - 1);

		// now add the free slot list
		int fslIndex = add(bb, "fsl", factory);
		fsl = getComponent(fslIndex);

		// now add the slots
		slotOverheads = new SlotOverhead[rootBlockSlotCount];

		for (int s = 0; s < rootBlockSlotCount; s++)
		{
			int index = add(bb, "so", factory);
			SlotOverhead slotOverhead = getComponent(index);
			slotOverhead.setSlotIndex(SafeCast.fromIntToUnsignedByte(s));
			slotOverhead.setPosition(slotOverhead.getStart());
			slotOverheads[s] = slotOverhead;
		}
	}

	public MetaData getMetaData()
	{
		return md;
	}

	public Sizing getSizing()
	{
		return sz;
	}

	public RepositoryOverhead getRepositoryOverhead()
	{
		return ro;
	}

	public Sandpit getSandpit()
	{
		return sandpit;
	}

	public BlockOverhead getBlockOverhead()
	{
		return bo;
	}

	public FreeSlotList getFreeSlotList()
	{
		return fsl;
	}
}

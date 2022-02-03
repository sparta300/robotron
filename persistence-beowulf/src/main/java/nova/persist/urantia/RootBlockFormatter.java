package nova.persist.urantia;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.SlotCountCalculator;
import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.RepositoryOverhead;
import org.hydroid.beowulf.overlay.Sandpit;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.mfdev.utility.SafeCast;

public class RootBlockFormatter implements BlockFormatter {
	private static final List<String> componentKeys = Arrays.asList("md", "sz", "ro", "sp", "bo");

	public void format(FormatterContext context) {
		ByteBuffer bb = context.bb;
		int blockSize = context.blockSize;
		int slotSize = context.slotSize;
		LocatorFactory locatorFactory = context.locatorFactory;
		OverlayFactory overlayFactory = context.overlayFactory;
		
		// sets start position
		PositionHelper posh = new PositionHelper(bb);

		// spin through and make each of the components
		overlayFactory.makeAll(componentKeys, bb);

		// go back to the beginning so we can read the components back in
		posh.start();
		
		MetaData md = new MetaData(bb, locatorFactory);
		Sizing sz = new Sizing(bb, locatorFactory);
		RepositoryOverhead ro = new RepositoryOverhead(bb, locatorFactory);
		Sandpit sp = new Sandpit(bb, locatorFactory);
		BlockOverhead bo = new BlockOverhead(bb, locatorFactory);

		// create a slot overhead just to see how big it is
		bb.mark();
		SlotOverhead so = overlayFactory.make("so", bb);
		int slotOverheadSize = so.size();
		bb.reset();

		// calculate the number of slots in the root block and in general
		// storage blocks
		SlotCountCalculator calculator = new SlotCountCalculator(blockSize, slotSize, posh.size(), bo.size(), slotOverheadSize);
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
		bo.setTotal(rootBlockSlotCount);
		bo.setFree(rootBlockSlotCount);
		bo.setUsed(0);

		// back the position up so that we can re-read the 'total' part of the block overhead.  This is a bit janky 
		// but maybe flr and fsl should just be one combined component.
		bo.end();
		bb.position(bb.position() - 1);

		// now add the free slot list
		FreeSlotList fsl = overlayFactory.make("fsl", bb);

		// now add the slots
		SlotOverhead[] slotOverheads = new SlotOverhead[rootBlockSlotCount];

		for (int slot = 0; slot < rootBlockSlotCount; slot++) {
			SlotOverhead slotOverhead = overlayFactory.make("so", bb);
			slotOverhead.setSlotIndex(SafeCast.fromIntToUnsignedByte(slot));
			slotOverhead.setPosition(slotOverhead.getStart());
			slotOverheads[slot] = slotOverhead;
		}
	}
}

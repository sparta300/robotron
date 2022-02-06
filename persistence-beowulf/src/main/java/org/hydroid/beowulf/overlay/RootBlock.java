package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.storage.LocatorFactory;

public class RootBlock extends CompositeOverlay implements RootOverlay {
	public RootBlock(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory) {
		super(bb, componentKeys, factory, locatorFactory);
		md = getComponent("md");
		sz = getComponent("sz");
		ro = getComponent("ro");
		sandpit = getComponent("sp");
		bo = getComponent("bo");
		
		// back the position up so that we can re-read the 'total' part of the block overhead
		bo.end();
		bb.position(bb.position() - 1);
		
		// now add the free slot list
		int fslIndex = add(bb, "fsl", factory);
		fsl = getComponent(fslIndex);
		
		int slotCount = sz.getRootBlockSlotCount();
		
		// now add the slots
		slotOverheads = new SlotOverhead[slotCount];
		buffers = new BufferReference[slotCount];
		int slotSize = sz.getSlotSize();
		
		for (int s = 0; s < slotCount; s++) {
			int index = add(bb, "so", factory);
			slotOverheads[s] = getComponent(index);;
		}
		
		for (int b = 0; b < slotCount; b++) {
			BufferReference buffer = new EagerBufferReference(b, bb, slotSize);
			buffers[b] = buffer;
			
			// move along a slot length
			bb.position(bb.position() + slotSize);
		}		
		
		markEnd();
	}
	
	
	public MetaData getMetaData() { return md; }
	public Sizing getSizing() { return sz; }
	public RepositoryOverhead getRepositoryOverhead() { return ro; }
	public Sandpit getSandpit() { return sandpit; }
	public BlockOverhead getBlockOverhead() { return bo; }
	public FreeSlotList getFreeSlotList() { return fsl; }
	public SlotOverhead[] getSlotOverheads() { return slotOverheads; }
	public BufferReference[] getBuffers() { return buffers; }
	
	private final MetaData md;
	private final Sizing sz;
	private final RepositoryOverhead ro;
	private final Sandpit sandpit;
	private final BlockOverhead bo;
	private final FreeSlotList fsl;
	private final BufferReference[] buffers;
	
	private SlotOverhead[] slotOverheads;
	
	private static final List<String> componentKeys = Arrays.asList("md", "sz", "ro", "sp", "bo");
}

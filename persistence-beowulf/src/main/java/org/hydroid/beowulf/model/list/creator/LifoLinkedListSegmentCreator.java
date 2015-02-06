package org.hydroid.beowulf.model.list.creator;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.overlay.CompositeOverlay;
import org.hydroid.beowulf.overlay.FreeListRuntime;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.IntPointer;
import com.lbg.persist.pointer.LongPointer;

/**
 * 
 * Superclass overlays:<br>
 * &nbsp;&nbsp;flr<br>
 * Constructor overlays:<br>
 * &nbsp;&nbsp;fsl<br>
 * &nbsp;&nbsp;ll-seg1 x n<br>
 * Reader overlay:<br>
 * &nbsp;&nbsp;SinglyLinkedListSegmentSlot
 * 
 * @author smiley
 */
public class LifoLinkedListSegmentCreator extends CompositeOverlay {
	public LifoLinkedListSegmentCreator(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory, Sizing sizing) {
		super(bb, overlayTypes, factory, locatorFactory);		
		int slotSize = sizing.getSlotSize();
		int totalSpace = slotSize - size();
		
		// create a list segment just to see how big it is
		bb.mark();
		SinglyLinkedListSegment testSegment = factory.make("ll-seg1", bb);
		int segmentSize = testSegment.size();
		bb.reset();		
		int freePointerListPos = bb.position();
		
		// now calculate how many segments we can squeeze in
		int spacePerSegment = 1 + segmentSize;
		int segmentCount = totalSpace / spacePerSegment;
		
		// now write the free slot list, which will consume one byte per linked list segment in this slot
		// back up so it can read the total
		FreeListRuntime flr = getComponent("flr");
		flr.setFree(segmentCount);
		flr.setUsed(0);
		flr.setTotal(segmentCount);
		bb.position(freePointerListPos - 1);
		add(bb, "fsl", factory);	
		
		// now write all the empty segments
		for (int s = 0; s < segmentCount; s++) {
			add(bb, "ll-seg1", factory);		
		}
		
		// remark the end, since we've added to the list of overlays added by our parent
        markEnd();
        
        logger.debug(String.format("slotSize=%d size=%d wilderness=%d", slotSize, size(), slotSize - size())); 
	}
    
    public long getHeadLocator(){ return headLocator.get(); }
    public long getTailLocator(){ return tailLocator.get(); }
    public long getItemType(){ return itemType.get(); }
    public int getMinorVersion(){ return minor.get(); }
    public int getBuildNumber(){ return build.get(); }
    
    private LongPointer headLocator;
    private LongPointer tailLocator;
    private LongPointer itemType;
    private IntPointer minor;
    private IntPointer build;
    
    private static final Logger logger = LoggerFactory.getLogger(LifoLinkedListSegmentCreator.class);
    
    private static final List<String> overlayTypes = Arrays.asList("flr");
}

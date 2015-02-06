package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_COUNT;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_SIZE;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.IntPointer;

public class Sizing extends AbstractOverlay {
    public Sizing(ByteBuffer bb, LocatorFactory locatorFactory) {        
    	super(bb, locatorFactory);
        blockSize = new IntPointer(bb);
        slotSize = new IntPointer(bb);
        indexSize = new IntPointer(bb);
        freeSlotPointerSize = new IntPointer(bb);
        repositoryOverheadSize = new IntPointer(bb);
        blockOverheadSize = new IntPointer(bb);
        rootBlockSlotCount = new IntPointer(bb);
        otherBlockSlotCount = new IntPointer(bb);
        markEnd();
        
        logger.debug(toString()); 
    }
    
    public Sizing(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {        
    	this(bb, locatorFactory);
    	
    	if (reset) {
    		reset();
    	}
    }    
 
    public void reset() {
    	ByteBuffer bb = getByteBuffer();
    	start();
        bb.putInt(UNSET_SIZE);     // blockSize
        bb.putInt(UNSET_SIZE);	   // slotSize
        bb.putInt(UNSET_SIZE);     // indexSize
        bb.putInt(UNSET_SIZE);     // freeSlotPointerSize
        bb.putInt(UNSET_SIZE);     // repositoryOverheadSize
        bb.putInt(UNSET_SIZE);     // blockOverheadSize
        bb.putInt(UNSET_COUNT);    // rootBlockSlotCount
        bb.putInt(UNSET_COUNT);    // otherSlotBlockCount
    }
    
    public String toString() {
    	final StringBuilder buf = new StringBuilder();
        buf.append("[b]=").append(blockSize.get());
        buf.append(" [s]=").append(slotSize.get());
        buf.append(" [so]=").append(indexSize.get());
        buf.append(" [fsp]=").append(freeSlotPointerSize.get());
        buf.append(" [ro]=").append(repositoryOverheadSize.get());        
        buf.append(" [bo]=").append(blockOverheadSize.get());
        buf.append(" *rbs=").append(rootBlockSlotCount.get());
        buf.append(" *obs=").append(otherBlockSlotCount.get());
        return buf.toString();
    }
    
    public void setBlockSize(int value){ blockSize.set(value); }
    public void setSlotSize(int value){ slotSize.set(value); }
    public void setSlotOverheadSize(int value){ indexSize.set(value); }
    public void setFreeSlotPointerSize(int value){ freeSlotPointerSize.set(value); }
    public void setRepositoryOverheadSize(int value){ repositoryOverheadSize.set(value); }
    public void setBlockOverheadSize(int value){ blockOverheadSize.set(value); }
    public void setRootBlockSlotCount(int value){ rootBlockSlotCount.set(value); }
    public void setOtherBlockSlotCount(int value){ otherBlockSlotCount.set(value); }
    
    public int getBlockSize(){ return blockSize.get(); }
    public int getSlotSize(){ return slotSize.get(); }
    public int getSlotOverheadSize(){ return indexSize.get(); }
    public int getFreeSlotPointerSize(){ return freeSlotPointerSize.get(); }
    public int getRepositoryOverheadSize(){ return repositoryOverheadSize.get(); }
    public int getBlockOverheadSize(){ return blockOverheadSize.get(); }
    public int getRootBlockSlotCount(){ return rootBlockSlotCount.get(); }
    public int getOtherBlockSlotCount(){ return otherBlockSlotCount.get(); }
    
    private IntPointer blockSize;
    private IntPointer slotSize;
    private IntPointer indexSize;
    private IntPointer freeSlotPointerSize;
    private IntPointer repositoryOverheadSize;
    private IntPointer blockOverheadSize;
    private IntPointer rootBlockSlotCount;
    private IntPointer otherBlockSlotCount;
    
    private static final Logger logger = LoggerFactory.getLogger(Sizing.class);
}

/**
 * File		: $Source:$
 * Author	: $Author:$
 * Revision	: $Revision:$
 * Date 	: $Date:$
 */
package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.RELEVANT_MASK_BIT_COUNT;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_INDEX;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_MASK;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_OBJECT_TYPE;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_POSITION;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_SIZE;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_SLOT_OVERLAY_TYPE;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.SlotOverlayType;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.IntPointer;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.pointer.UnsignedBytePointer;

/**
 * @author smiley
 */
public class SlotOverhead extends AbstractOverlay {

    public SlotOverhead(ByteBuffer buffer, LocatorFactory locatorFactory) {   
    	super(buffer, locatorFactory);
        slotIndex = new UnsignedBytePointer(buffer);
        mask = new IntPointer(buffer);
        type = new LongPointer(buffer);
        position = new LongPointer(buffer);        
        locator = new LongPointer(buffer);
        nextLocator = new LongPointer(buffer);
        redirectLocator = new LongPointer(buffer);        
        multiVersionLocator = new LongPointer(buffer);
        metaDataLocator = new LongPointer(buffer);
        slotOverlayTypePointer = new IntPointer(buffer);
        totalRawSize = new IntPointer(buffer);
        totalStoredSize = new IntPointer(buffer);                                      
        markEnd();
    }
    
    public SlotOverhead(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset){
        this(bb, locatorFactory);      

		if (reset) {
			reset();
		}	
    }
    
    public void reset() {
    	ByteBuffer bb = getByteBuffer();
    	start();
        bb.put(SafeCast.fromIntToUnsignedByte(UNSET_INDEX));	// slotIndex
        bb.putInt(UNSET_MASK);									// mask  
        bb.putLong(UNSET_OBJECT_TYPE);							// serialised object type
        bb.putLong(UNSET_POSITION);								// position
        bb.putLong(UNSET_LOCATOR);								// locator
        bb.putLong(UNSET_LOCATOR);								// nextLocator
        bb.putLong(UNSET_LOCATOR);								// redirectLocator
        bb.putLong(UNSET_LOCATOR);								// multiVersionLocator
        bb.putLong(UNSET_LOCATOR);								// metaDataLocator
        bb.putInt(UNSET_SLOT_OVERLAY_TYPE);						// slotOverlayType
        bb.putInt(UNSET_SIZE);									// totalRawSize
        bb.putInt(UNSET_SIZE);									// totalStoredSize	     
		logger.debug(toString());
    }
    
    public String toString(){
    	final StringBuilder buf = new StringBuilder();
        buf.append("<s").append(slotIndex.get()).append(">");              
        buf.append(" pos=");
        buf.append(position.get());  
        buf.append(" mask=");
        
        if (mask.get() == UNSET_MASK)
        {
            buf.append("?");
        }
        else
        {                
            for (int i = 63; i > -1; i--)
                if (i < RELEVANT_MASK_BIT_COUNT)
                {
    			    buf.append((mask.get() & (1L << i)) > 0L ? "1" : "0");
                }
     		
		    buf.append(" (hex=" + Long.toHexString(mask.get()) + ")");
		}

        if (locator.get() == UNSET_LOCATOR)
        {
            buf.append(" &=?");
        }
        else
        {
            buf.append(" &=");
            buf.append(locator.get());
        }

        if (nextLocator.get() == UNSET_LOCATOR)
        {
            buf.append(" next&=?");
        }
        else
        {
            buf.append(" next&=");
            buf.append(nextLocator.get());
        }
        
        if (redirectLocator.get() == UNSET_LOCATOR)
        {
            buf.append(" redirect&=?");
        }
        else
        {
            buf.append(" redirect&=");
            buf.append(redirectLocator.get());
        }       
        
        if (multiVersionLocator.get() == UNSET_LOCATOR)
        {
            buf.append(" multi&=?");
        }
        else
        {
            buf.append(" multi&=");
            buf.append(multiVersionLocator.get());
        }   
        
        if (metaDataLocator.get() == UNSET_LOCATOR)
        {
            buf.append(" meta&=?");
        }
        else
        {
            buf.append(" meta&=");
            buf.append(metaDataLocator.get());
        }         
        
        if (slotOverlayTypePointer.get() == UNSET_SLOT_OVERLAY_TYPE)
        {
            buf.append(" overlayType=?");
        }
        else
        {
            buf.append(" overlay=");
            buf.append(getSlotOverlayType());
        }  
        
        if (totalRawSize.get() == UNSET_SIZE)
        {
            buf.append(" [raw]=?");
        }
        else
        {
            buf.append(" [raw]=");
            buf.append(totalRawSize.get());
        } 
        
        if (totalStoredSize.get() == UNSET_SIZE)
        {
            buf.append(" [stored]=?");
        }
        else
        {
            buf.append(" [stored]=");
            buf.append(totalStoredSize.get());
        }                                     
        
        return buf.toString();
    }
    
    public void setSlotIndex(int value){ slotIndex.set(value); }
    public void setMask(int value){ mask.set(value); }
    public void setType(long value){ type.set(value); }
    public void setPosition(long value){ position.set(value); }
    public void setLocator(long value){ locator.set(value); }
    public void setNextLocator(long value){ nextLocator.set(value); }
    public void setRedirectLocator(long value){ redirectLocator.set(value); }
    public void setTotalRawSize(int value){ totalRawSize.set(value); }
    public void setTotalStoredSize(int value){ totalStoredSize.set(value); }
    public void setMultiVersionLocator(long value){ multiVersionLocator.set(value); }
    public void setMetaDataLocator(long value){ metaDataLocator.set(value); }    
    public void setSlotOverlayType(int value){ slotOverlayTypePointer.set(value); } 
            
    public int getSlotIndex(){ return slotIndex.get(); }
    public int getMask(){ return mask.get(); }
    public long getType(){ return type.get(); }
    public long getLocator(){ return locator.get(); }
    public long getNextLocator(){ return nextLocator.get(); }
    public long getRedirectLocator(){ return redirectLocator.get(); }
    public int getTotalRawSize(){ return totalRawSize.get(); }
    public int getTotalStoredSize(){ return totalStoredSize.get(); }
    public long getPosition(){ return position.get(); }
    public long getMultiVersionLocator(){ return multiVersionLocator.get(); }
    public long getMetaDataLocator(){ return metaDataLocator.get(); }
    
    public SlotOverlayType getSlotOverlayType(){ return SlotOverlayType.fromInt(slotOverlayTypePointer.get()); } 
    
    public IntPointer getMaskPointer(){ return mask; }
    
    private UnsignedBytePointer slotIndex;
    private IntPointer mask;
    private LongPointer type;
    private LongPointer position;
    private LongPointer locator;
    private LongPointer nextLocator;
    private LongPointer redirectLocator;
    private LongPointer metaDataLocator;
    private IntPointer totalRawSize;
    private IntPointer totalStoredSize;
    private LongPointer multiVersionLocator;
    private IntPointer slotOverlayTypePointer;
   
    private static final Logger logger = LoggerFactory.getLogger(SlotOverhead.class);
}


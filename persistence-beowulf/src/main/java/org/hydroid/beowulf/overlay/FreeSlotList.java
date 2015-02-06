package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.UnsignedBytePointer;

/**
 * a list of free slots.  Each position in the list either contains a slot ID of a free slot or the value SLOT_USED.
 * 
 * @author smiley
 *
 */
public class FreeSlotList extends AbstractOverlay {
	public FreeSlotList(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);
		slotCount = new UnsignedBytePointer(bb).get();
		restart();
		freeSlotPointers = new UnsignedBytePointer[slotCount];
		
		for (int s = 0; s < slotCount; s++) {
			freeSlotPointers[s] = new UnsignedBytePointer(bb);
		}
		
		markEnd();
		logger.debug(toString());
	}
	
	public FreeSlotList(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
		super(bb, locatorFactory);
		slotCount = new UnsignedBytePointer(bb).get();
		restart();
		freeSlotPointers = new UnsignedBytePointer[slotCount];
		
		if (reset) {
			reset();
		} else {
			load(bb);
		}
		
		markEnd();
	}	
	 
	private void load(ByteBuffer bb) {
		for (int s = 0; s < slotCount; s++) {
			freeSlotPointers[s] = new UnsignedBytePointer(bb);
			freeSlotPointers[s].get();
		}
	}

	@Override
	public void reset() {
		ByteBuffer bb = getByteBuffer();
		start();
		
		for (int s = 0; s < slotCount; s++) {
			freeSlotPointers[s] = new UnsignedBytePointer(bb);
			freeSlotPointers[s].set(SafeCast.fromIntToUnsignedByte(s));
		}
	}
	
    public void stringify(StringBuilder buf) {
        buf.append(EOL);
        
        for (int p = 0; p < this.slotCount; p++) {

            if (freeSlotPointers[p].get() == SLOT_USED){
                buf.append("[" + p + "] in use");
                buf.append(EOL);
            } else {
                buf.append("[" + p + "] free");
                buf.append(EOL);
            }
        }
    }
    
    public int getPointerValue(int index) {
    	return freeSlotPointers[index].get();
    }
    
    public UnsignedBytePointer getPointer(int index) {
    	return freeSlotPointers[index];
    }    
    
    public String toString() {
    	final StringBuilder buf = new StringBuilder();
        stringify(buf);
        return buf.toString();
    }	
    
    public int getSlotCount() { return slotCount; }
    public UnsignedBytePointer[] getPointers() { return freeSlotPointers; }

	private static final Logger logger = LoggerFactory.getLogger(FreeSlotList.class);
	
	private static final String EOL = System.getProperty("line.separator");
	
	private final UnsignedBytePointer[] freeSlotPointers;
	private final int slotCount;
}

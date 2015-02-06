package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_COUNT;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.UnsignedBytePointer;

/**
 * a simple, list-based space manager that can manage up to 256 pointers, where each pointer is represented by
 * a single byte.
 * 
 * @author smiley
 */
public class FreeListRuntime extends AbstractOverlay {
	public FreeListRuntime(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);
		free = new UnsignedBytePointer(bb);
		used = new UnsignedBytePointer(bb);
		total = new UnsignedBytePointer(bb);		
		markEnd();
	}	
	
	public FreeListRuntime(ByteBuffer bb, LocatorFactory locatorFactory, boolean create) {
		super(bb, locatorFactory);
		free = new UnsignedBytePointer(bb);
		used = new UnsignedBytePointer(bb);
		total = new UnsignedBytePointer(bb);
		
		if (create) {
			reset();
		}
		
		markEnd();
	}
	
	public void reset() {
		ByteBuffer bb = getByteBuffer();
		start();				
		bb.put(SafeCast.fromIntToUnsignedByte(UNSET_COUNT));
		bb.put(SafeCast.fromIntToUnsignedByte(UNSET_COUNT));
		bb.put(SafeCast.fromIntToUnsignedByte(UNSET_COUNT));
	}
	
	public String toString() {
		return String.format(" *free=%d *used=%d *total=%d", getFree(), getUsed(), getTotal()); 
	}
	
	public void setUsed(int value) { used.set(value); }
	public void setFree(int value) { free.set(value); }	
	public void setTotal(int value) { total.set(value);	}	
	
	public int getUsed() { return used.get(); }
	public int getFree() { return free.get(); }	
	public int getTotal() {	return total.get(); }	
	
	private UnsignedBytePointer total;
	private UnsignedBytePointer used;
	private UnsignedBytePointer free;	
}

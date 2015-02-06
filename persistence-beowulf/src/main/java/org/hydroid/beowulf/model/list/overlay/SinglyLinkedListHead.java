/*
 * Created on Apr 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.hydroid.beowulf.model.list.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.overlay.AbstractOverlay;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.IntPointer;
import com.lbg.persist.pointer.LongPointer;

/**
 * @author smiley
 */
public class SinglyLinkedListHead extends AbstractOverlay {
	public SinglyLinkedListHead(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);		
		headSegmentLocator = new LongPointer(bb);
		tailSegmentLocator = new LongPointer(bb);
		length = new IntPointer(bb);
		markEnd();
	}

	public SinglyLinkedListHead(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
		this(bb, locatorFactory);
		
		if (reset) {
			reset();
		}
	}	
	
	public void reset() {
		ByteBuffer buffer = getByteBuffer();
		start();
		buffer.putLong(UNSET_LOCATOR);
		buffer.putLong(UNSET_LOCATOR);
		buffer.putInt(0);
		logger.debug("reset(): " + toString());		
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendPointer(sb, "head", headSegmentLocator, false, true);
		appendPointer(sb, "tail", tailSegmentLocator, false, true);
		sb.append("length=").append(length.get());
		return sb.toString();
	}

	public void setLength(int value) { length.set(value); }	
	public void setHeadSegmentLocator(long value) { headSegmentLocator.set(value);	}
	public void setTailSegmentLocator(long value) { tailSegmentLocator.set(value);	}	

	public int getLength() { return length.get(); }	
	public long getHeadSegmentLocator() { return headSegmentLocator.get(); }
	public long getTailSegmentLocator() { return tailSegmentLocator.get(); }

	private IntPointer length;
	private LongPointer headSegmentLocator;
	private LongPointer tailSegmentLocator;

	private static final Logger logger = LoggerFactory.getLogger(SinglyLinkedListHead.class);
}



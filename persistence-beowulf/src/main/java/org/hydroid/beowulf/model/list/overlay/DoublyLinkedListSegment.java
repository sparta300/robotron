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

import com.lbg.persist.pointer.LongPointer;

/**
 * @author smiley
 */
public class DoublyLinkedListSegment extends AbstractOverlay {
	public DoublyLinkedListSegment(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);
		dataLocator = new LongPointer(bb);
		previousSegmentLocator = new LongPointer(bb);
		nextSegmentLocator = new LongPointer(bb);
		markEnd();
		logger.debug(toString());
	}

	public DoublyLinkedListSegment(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
		super(bb, locatorFactory);
		// can't try to set block type to an enum just yet in case we are reading garbage
		dataLocator = new LongPointer(bb);	
		previousSegmentLocator = new LongPointer(bb);
		nextSegmentLocator = new LongPointer(bb);		
		markEnd();
		
		if (reset) {
			reset();
		}
	}	

	public void reset() {
		ByteBuffer buffer = getByteBuffer();
		start();
		buffer.putLong(UNSET_LOCATOR);
		buffer.putLong(UNSET_LOCATOR);
		buffer.putLong(UNSET_LOCATOR);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("data&=").append(dataLocator.get());
		sb.append(" previous&=").append(previousSegmentLocator.get());
		sb.append(" next&=").append(nextSegmentLocator.get());
		return sb.toString();
	}

	public void getDataLocator(long value) { dataLocator.set(value); }
	public void getPreviousSegmentLocator(long value) { previousSegmentLocator.set(value);	}	
	public void getNextSegmentLocator(long value) { nextSegmentLocator.set(value);	}	

	public long getDataLocator() { return dataLocator.get(); }
	public long getPreviousSegmentLocator() { return previousSegmentLocator.get(); }
	public long getNextSegmentLocator() { return nextSegmentLocator.get(); }

	private LongPointer dataLocator;
	private LongPointer previousSegmentLocator;
	private LongPointer nextSegmentLocator;

	private static final Logger logger = LoggerFactory.getLogger(DoublyLinkedListSegment.class);
}

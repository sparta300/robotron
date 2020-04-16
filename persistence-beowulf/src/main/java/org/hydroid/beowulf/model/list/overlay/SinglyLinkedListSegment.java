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
public class SinglyLinkedListSegment extends AbstractOverlay {
	private static final Logger logger = LoggerFactory.getLogger(SinglyLinkedListSegment.class);

	private final LongPointer dataLocator;
	private final LongPointer nextSegmentLocator;

	public SinglyLinkedListSegment(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);
		dataLocator = new LongPointer(bb);
		nextSegmentLocator = new LongPointer(bb);
		markEnd();
	}

	public SinglyLinkedListSegment(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
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
		logger.debug(String.format("reset(): %s", toString()));
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		appendDataPointer(sb, "data", dataLocator, false, true);
		appendPointer(sb, "next", nextSegmentLocator, false, false);
		sb.append(System.getProperty("line.separator"));
		return sb.toString();
	}

	public void setDataLocator(long value) { dataLocator.set(value); }	
	public void setNextSegmentLocator(long value) { nextSegmentLocator.set(value); }	

	public long getDataLocator() { return dataLocator.get(); }	
	public long getNextSegmentLocator() { return nextSegmentLocator.get(); }	
}

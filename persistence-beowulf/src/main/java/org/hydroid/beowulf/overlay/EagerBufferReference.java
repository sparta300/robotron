package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * a buffer reference with an eager-loading policy.
 * 
 * @author smiley
 *
 */
public class EagerBufferReference implements BufferReference
{
	private static final Logger log = LoggerFactory.getLogger(EagerBufferReference.class);

	private ByteBuffer slice;
	
	public EagerBufferReference(int slotIndex, ByteBuffer bb, int slotSize)
	{
		// create a view buffer for the slice of the buffer representing this
		// slot
		int pos = bb.position();
		int newLimit = pos + slotSize;

		log.debug("slots[" + slotIndex + "] pos=" + pos + " newLimit=" + newLimit);

		bb.limit(newLimit);
		slice = bb.slice();
		// logger.debug("sliced buffer size " + slots[s].capacity());
	}

	public ByteBuffer dereference()
	{
		return slice;
	}
}

package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;

/**
 * a reference to a slot buffer, allowing you to have a policy of when you might
 * like to create a slice buffer.
 * 
 * @author smiley
 *
 */
public interface BufferReference
{
	ByteBuffer dereference();
}

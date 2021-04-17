package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;

public interface Overlay
{
	long getStart();

	long getEnd();

	/**
	 * set position to start position.
	 */
	void start();

	/**
	 * changes the start position to be the current position.
	 */
	void restart();

	/**
	 * set position to end position.
	 */
	void end();

	/**
	 * reset overlay to factory settings
	 */
	void reset();

	/**
	 * @return tracker used for calculations
	 */
	// OverlayTracker getTracker();

	ByteBuffer getByteBuffer();

	int size();

	String report();
}

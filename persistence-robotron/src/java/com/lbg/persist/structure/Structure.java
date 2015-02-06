package com.lbg.persist.structure;

import java.nio.ByteBuffer;

/**
 * a structure is a basic component of any block but is nothing more complicated than a contiguous chunk of space.
 * 
 * @author C006011
 */
public interface Structure
{
	int getStart();

	int getEnd();

	/**
	 * set position to start position.
	 */
	void moveToStart();

	/**
	 * set position to end position.
	 */
	void moveToEnd();	

	/**
	 * reset to factory settings
	 */
	void reset();

	int size();
	
	ByteBuffer getByteBuffer();
}

package com.lbg.persist.daemon;

import java.nio.ByteBuffer;

/**
 * represents a loaded page.
 * 
 * @author smiley
 */
public interface Page
{
	int getIndex();

	PageIdentifier getIdentifier();

	ByteBuffer getByteBuffer();
	
	Buffer getBuffer();

	void flush();
}

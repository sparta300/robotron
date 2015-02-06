package com.lbg.persist.daemon;

import java.nio.ByteBuffer;

public interface Buffer
{
	boolean isScratch();
	ByteBuffer getByteBuffer();
	int getPosition();
}

package com.lbg.persist.daemon;

import java.nio.ByteBuffer;

/**
 * an implementation of a scratch buffer that is backed by a byte buffer.
 * 
 * @author C006011
 */
public class ScratchBufferImpl implements ScratchBuffer
{
	private final ByteBuffer bb;
	
	public ScratchBufferImpl(int size)
	{
		bb = ByteBuffer.allocate(size);
	}
	 
	public ScratchBufferImpl(byte[] data)
	{
		bb = ByteBuffer.wrap(data);
	}	

	public boolean isScratch()
	{
		return true;
	}
	
	public ByteBuffer getByteBuffer()
	{
		return bb;
	}

	@Override
	public int getPosition()
	{
		return bb.position();
	}

	@Override
	public void clear()
	{
		bb.clear();
	}
}

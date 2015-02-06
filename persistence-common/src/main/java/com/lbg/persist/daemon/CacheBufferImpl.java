package com.lbg.persist.daemon;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheBufferImpl implements CacheBuffer
{
	private static final Logger log = LoggerFactory.getLogger(CacheBufferImpl.class);
	
	private MappedByteBuffer mbb;
	private ByteBuffer bb;
	
	public CacheBufferImpl(MappedByteBuffer mbb, ByteBuffer bb)
	{
		this.mbb = mbb;
		this.bb = bb;
	}

	@Override
	public boolean isScratch()
	{
		return false;
	}

	@Override
	public void flush()
	{
		log.debug("flushing cache buffer");
		mbb.force();
	}

	@Override
	public ByteBuffer getByteBuffer()
	{
		return bb;
	}

	@Override
	public int getPosition()
	{
		return bb.position();
	}
}

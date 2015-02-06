package com.lbg.persist.daemon;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageImpl implements Page
{
	private static final Logger log = LoggerFactory.getLogger(PageImpl.class);
	
	private Buffer buffer;
	private PageIdentifier pageId;
	private int index;
	
	public PageImpl(PageIdentifier pageId, Buffer buffer, int index)
	{
		this.pageId = pageId;
		this.buffer = buffer;
		this.index = index;
	}

	public void flush()
	{
		if (buffer.isScratch())
		{
			log.debug("ignoring flush on scratch buffer");
			return;			
		}
		
		if (!(buffer instanceof CacheBuffer))
		{
			log.warn("ignoring flush buffer when not a cache buffer");
			return;
		}
		
		log.debug("flushing cache slot " + index);
		((CacheBuffer)buffer).flush();
	}

	@Override
	public ByteBuffer getByteBuffer()
	{
		return buffer.getByteBuffer();
	}
	
	@Override
	public Buffer getBuffer()
	{
		return buffer;
	}

	public PageIdentifier getIdentifier()
	{
		return pageId;
	}

	public int getIndex()
	{
		return index;
	}

	@Override	
	public String toString()
	{
		return String.format("page at index %d - %s", index, pageId.toString());
	}
}

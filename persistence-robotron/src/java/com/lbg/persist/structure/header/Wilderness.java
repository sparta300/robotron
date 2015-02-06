package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.structure.raw.Header;

/**
 * wilderness is just a name for a chunk of block space that is currently unused.
 * 
 * @author C006011
 */
public class Wilderness extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(Wilderness.class);
	
	private final String stringified;
	
	public Wilderness(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}
	
	private Wilderness(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		final int space = header.getDataSize();
		stringified = "wilderness space=" + space;
		bb.position(bb.position() + space);
		markEnd();
		
		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(stringified);
		}
	}
	
	public static Wilderness forge(ByteBuffer bb, Header header)
	{
		return new Wilderness(bb, header, true);
	}

	@Override
	public void reset()
	{
		moveToStart(); 
		final ByteBuffer bb = getByteBuffer();
		
		// this will explicitly zero out the wilderness
		for (int p = getStart(); p < getEnd(); p++)
		{
			bb.put(p, (byte)0);
		}
		
		moveToEnd();
	}

	@Override
	public String toString()
	{
		return stringified;
	}	
}

package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import com.lbg.persist.structure.Structure;
import com.lbg.persist.structure.raw.Header;

/**
 * a data structure that is always preceeded by a header.
 * 
 * @author C006011
 */
public abstract class AbstractStructureWithHeader implements Structure
{
	private final int startPosition;
	private final ByteBuffer bb;
	private final Header header;

	private int endPosition;
	private int size;

	protected AbstractStructureWithHeader(ByteBuffer bb, Header header)
	{
		this.bb = bb;
		startPosition = bb.position();
		this.header = header;
	}

	@Override
	public int getStart()
	{
		return startPosition;
	}

	protected void markEnd()
	{
		endPosition = bb.position();
		size = endPosition - startPosition;
	}
	
	@Override
	public int getEnd()
	{
		return endPosition;
	}

	@Override
	public void moveToStart()
	{
		bb.position(startPosition);
	}

	@Override
	public void moveToEnd()
	{
		bb.position(endPosition);
	}

	@Override
	public ByteBuffer getByteBuffer()
	{
		return bb;
	}

	@Override
	public int size()
	{
		return size;
	}

	public Header getHeader()
	{
		return header;
	}
	
	final public int getElementCount()
	{
		return header.getElementCount();
	}
}

package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.SafeCast;

/**
 * contains some common behaviour for basic structures.
 * @author C006011
 */
abstract public class AbstractStructure implements Structure
{
	private final ByteBuffer bb;

	private int size;
	private int endPosition;
	private int startPosition;

	protected AbstractStructure(ByteBuffer bb)
	{
		this.bb = bb;
		this.startPosition = bb.position();
	}

	public int getStart()
	{
		return startPosition;
	}

	public int getEnd()
	{
		return endPosition;
	}

	protected void markEnd()
	{
		endPosition = bb.position();
		size = SafeCast.fromLongToInt(endPosition - startPosition);
	}

	public ByteBuffer getByteBuffer()
	{
		return bb;
	}

	public void moveToStart()
	{
		bb.position(SafeCast.fromLongToInt(startPosition));
	}

	public void restart()
	{
		startPosition = bb.position();
	}

	public void moveToEnd()
	{
		bb.position(SafeCast.fromLongToInt(endPosition));
	}

	public int size()
	{
		return size;
	}

	public String report()
	{
		return String.format("start=%4d end=%4d size=%4d", startPosition, endPosition, size);
	}

	
}

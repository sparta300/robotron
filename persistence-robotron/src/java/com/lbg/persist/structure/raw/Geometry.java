package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.AbstractStructure;

/**
 * stores some basic information about the geometry of the file.
 * 
 * @author C006011
 */
public class Geometry extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(Geometry.class);

	private final ShortPointer blockSize;
	private final ShortPointer maxBlockCount;

	public Geometry(ByteBuffer bb)
	{
		this(bb, false);
	}

	private Geometry(ByteBuffer bb, boolean reset)
	{
		super(bb);
		blockSize = new ShortPointer(bb);
		maxBlockCount = new ShortPointer(bb);
		markEnd();

		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(toString());
		}
	}
	
	public static Geometry forge(ByteBuffer bb)
	{
		return new Geometry(bb, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.putShort(SafeCast.fromIntToShort(Unset.SIZE));
		bb.putShort(SafeCast.fromIntToShort(Unset.COUNT));
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append("[b]=").append(blockSize.get());
		buf.append(" *maxb=").append(maxBlockCount.get());
		return buf.toString();
	}

	public int getBlockSize()
	{
		return blockSize.get();
	}

	public int getMaxBlockCount()
	{
		return maxBlockCount.get();
	}
	
	public void setBlockSize(int blockSize)
	{
		this.blockSize.set(blockSize);
	}

	public void setMaxBlockCount(int maxBlockCount)
	{
		this.maxBlockCount.set(maxBlockCount);
	}
}

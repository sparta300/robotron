package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.AbstractStructure;

/**
 * a header that tells you the type and the size of the following overlay.
 * 
 * @author C006011
 */
public class Header extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(Header.class);

	private final ShortPointer type;
	private final ShortPointer dataSize;
	private final ShortPointer elementCount;

	public Header(ByteBuffer bb)
	{
		super(bb);
		type = new ShortPointer(bb);
		dataSize = new ShortPointer(bb);
		elementCount = new ShortPointer(bb);
		markEnd();
		log.debug(toString());
	}

	private Header(ByteBuffer bb, boolean reset)
	{
		super(bb);

		type = new ShortPointer(bb);
		dataSize = new ShortPointer(bb);
		elementCount = new ShortPointer(bb);
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
	
	public static Header forge(ByteBuffer bb)
	{
		return new Header(bb, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putShort(SafeCast.fromIntToShort(Unset.STRUCTURE_TYPE));
		buffer.putShort(SafeCast.fromIntToShort(Unset.SIZE));
		buffer.putShort(SafeCast.fromIntToShort(Unset.COUNT));
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("type=").append(type.get());
		sb.append(" dataSize=").append(dataSize.get());
		sb.append(" elementCount=").append(elementCount.get());
		return sb.toString();
	}

	public void setType(short value)
	{
		type.set(value);
	}

	public void setDataSize(short value)
	{
		dataSize.set(value);
	}
	
	public void setElementCount(short value)
	{
		elementCount.set(value);
	}	

	public int getType()
	{
		return type.get();
	}

	public int getDataSize()
	{
		return dataSize.get();
	}
	
	public int getElementCount()
	{
		return elementCount.get();
	}
}

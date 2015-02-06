package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * a 16 bit count, usually used for element counts in lists.
 * 
 * @author C006011
 */
public class Count16 extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(Count16.class);

	private final ShortPointer count;

	public Count16(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private Count16(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);

		// can't try to set block type to an enum just yet in case we are
		// reading garbage
		count = new ShortPointer(bb);
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
	
	public static Count16 forge(ByteBuffer bb, Header header)
	{
		return new Count16(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putShort(SafeCast.fromIntToShort(Unset.COUNT));
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("count=").append(count.get());
		return sb.toString();
	}
	
	public void setCount(short value)
	{
		count.set(value);
	}	
	
	public int getCount()
	{
		return count.get();
	}	
}

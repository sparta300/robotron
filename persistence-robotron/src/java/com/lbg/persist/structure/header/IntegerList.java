package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Unset;
import com.lbg.persist.pointer.IntPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * a header that tells you the type and the size of the following overlay.
 * 
 * @author C006011
 */
public class IntegerList extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(IntegerList.class);

	private final IntPointer[] pointers;
	private final int elementCount;
	
	public IntegerList(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}
	
	private IntegerList(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		this.elementCount = header.getElementCount();
		pointers = new IntPointer[elementCount];
		
		for (int e = 0; e < elementCount; e++)
		{
			pointers[e] = new IntPointer(bb);
		}
		
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
	
	public static IntegerList forge(ByteBuffer bb, Header header)
	{
		return new IntegerList(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		
		for (int e = 0; e < elementCount; e++)
		{
			buffer.putInt(Unset.IDENTIFIER);
		}
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("elementCount=").append(elementCount);
		
		for (int e = 0; e < elementCount; e++)
		{
			sb.append(" [").append(e).append("]=").append(get(e));
		}
		
		return sb.toString();
	}

	public void set(int index, int value)
	{
		pointers[index].set(value);
	}
	
	public int get(int index)
	{
		return pointers[index].get();
	}
}

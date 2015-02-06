package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.DoublePointer;
import com.lbg.persist.structure.raw.Header;

/**
 * a header that tells you the type and the size of the following overlay.
 * 
 * @author C006011
 */
public class DoubleList extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(DoubleList.class);

	private final DoublePointer[] pointers;
	private final int elementCount;
	
	public DoubleList(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}
	
	private DoubleList(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		this.elementCount = header.getElementCount();
		pointers = new DoublePointer[elementCount];
		
		for (int e = 0; e < elementCount; e++)
		{
			pointers[e] = new DoublePointer(bb);
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
	
	public static DoubleList forge(ByteBuffer bb, Header header)
	{
		return new DoubleList(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		
		for (int e = 0; e < elementCount; e++)
		{
			buffer.putDouble(0d);
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

	public void set(int index, double value)
	{
		pointers[index].set(value);
	}
	
	public double get(int index)
	{
		return pointers[index].get();
	}
}

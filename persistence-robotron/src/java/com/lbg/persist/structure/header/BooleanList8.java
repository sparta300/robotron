package com.lbg.persist.structure.header;

import static org.hydroid.file.FileConstants.BOOLEAN_BYTE_FALSE;
import static org.hydroid.file.FileConstants.BOOLEAN_BYTE_TRUE;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.BytePointer;
import com.lbg.persist.structure.raw.Header;

/**
 * a header that tells you the type and the size of the following overlay.
 * 
 * @author C006011
 */
public class BooleanList8 extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(BooleanList8.class);

	private final BytePointer[] pointers;
	private final int elementCount;
	
	public BooleanList8(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}
 
	private BooleanList8(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		this.elementCount = header.getElementCount();
		pointers = new BytePointer[elementCount];
		
		for (int e = 0; e < elementCount; e++)
		{
			pointers[e] = new BytePointer(bb);
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
	
	public static BooleanList8 forge(ByteBuffer bb, Header header)
	{
		return new BooleanList8(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		
		for (int e = 0; e < elementCount; e++)
		{
			buffer.put(SafeCast.fromIntToByte(Unset.BOOLEAN_BYTE));
		}
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("elementCount=").append(elementCount);
		
		for (int e = 0; e < elementCount; e++)
		{
			final byte value = get(e);
			sb.append(" [" + e + "]=");
			
			if (value == BOOLEAN_BYTE_TRUE)
			{
				sb.append("t");
			} 
			else if (value == BOOLEAN_BYTE_FALSE)
			{
				sb.append("f");
			}
			else
			{
				sb.append("?");
			}
		}
		
		return sb.toString();
	}

	public void set(int index, boolean value)
	{
		pointers[index].set(value ? BOOLEAN_BYTE_TRUE : BOOLEAN_BYTE_FALSE);
	}
	
	public byte get(int index)
	{
		return SafeCast.fromIntToByte(pointers[index].get());
	}
	
	public BytePointer getPointer(int index)
	{
		return pointers[index];
	}
}

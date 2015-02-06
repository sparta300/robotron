package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Unset;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * a simple object to store basic relationships between stored objects.
 * 
 * @author C006011
 */
public class RelationshipSet extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(RelationshipSet.class);

	private final LongPointer parentAddress;
	private final LongPointer childAddress;
	private final LongPointer previousAddress;
	private final LongPointer nextAddress;

	public RelationshipSet(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private RelationshipSet(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);

		// can't try to set block type to an enum just yet in case we are
		// reading garbage
		parentAddress = new LongPointer(bb);
		childAddress = new LongPointer(bb);
		previousAddress = new LongPointer(bb);
		nextAddress = new LongPointer(bb);
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
	
	public static RelationshipSet forge(ByteBuffer bb, Header header)
	{
		return new RelationshipSet(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putLong((long)Unset.ADDRESS);
		buffer.putLong((long)Unset.ADDRESS);
		buffer.putLong((long)Unset.ADDRESS);
		buffer.putLong((long)Unset.ADDRESS);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("parent=").append(parentAddress.get());
		sb.append(" childAddress=").append(childAddress.get());
		sb.append(" previous=").append(previousAddress.get());
		sb.append(" next=").append(nextAddress.get());
		return sb.toString();
	}
	
	public long getParentAddress()
	{
		return parentAddress.get();
	}

	public long getChildAddress()
	{
		return childAddress.get();
	}

	public long getPreviousAddress()
	{
		return previousAddress.get();
	}

	public long getNextAddress()
	{
		return nextAddress.get();
	}
	
	public void setParentAddress(long address)
	{
		parentAddress.set(address);
	}

	public void setChildAddress(long address)
	{
		childAddress.set(address);
	}

	public void setPreviousAddress(long address)
	{
		previousAddress.set(address);
	}

	public void setNextAddress(long address)
	{
		nextAddress.set(address);
	}	
}

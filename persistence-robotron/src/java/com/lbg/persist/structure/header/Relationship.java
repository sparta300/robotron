package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.RelationshipType;
import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.BytePointer;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * stores a relationship to another object by defining the relationship type 
 * and the address of the related object.
 * 
 * @author C006011
 */
public class Relationship extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(Relationship.class);

	private final BytePointer relationshipType;
	private final LongPointer relatedAddress;

	public Relationship(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private Relationship(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		relationshipType = new BytePointer(bb);
		relatedAddress = new LongPointer(bb);
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
	
	public Relationship forge(ByteBuffer bb, Header header)
	{
		return new Relationship(bb, header, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.put(SafeCast.fromIntToByte(Unset.SIZE));
		bb.putLong((long)Unset.ADDRESS);
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append("type=").append(relationshipType.get());
		buf.append(" address=").append(relatedAddress.get());
		return buf.toString();
	}

	public int getRelationshipType()
	{
		return relationshipType.get();
	}
	
	public void setRelationshipType(RelationshipType type)
	{
		this.relationshipType.set(SafeCast.fromIntToByte(type.getTypeId()));
	}	

	public long getRelatedAddress()
	{
		return relatedAddress.get();
	}

	public void setRelatedAddress(long address)
	{
		this.relatedAddress.set(address);
	}
}

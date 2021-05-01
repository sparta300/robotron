package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.PersistConstants;
import com.lbg.persist.pointer.IntPointer;
import com.lbg.persist.structure.AbstractStructure;
import com.mfdev.utility.BaseUtils;

/**
 * a magic number to help operating systems identify this file type.
 * 
 * @author C006011
 */
public class Magic extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(Magic.class);

	private final IntPointer magic;

	public Magic(ByteBuffer bb)
	{
		this(bb, false);
	}

	private Magic(ByteBuffer bb, boolean reset)
	{
		super(bb);
		magic = new IntPointer(bb);
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
	
	public static Magic forge(ByteBuffer bb)
	{
		return new Magic(bb, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.putInt(PersistConstants.MAGIC);
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append("0x");
		buf.append(BaseUtils.toHexString(magic.get()));
		return buf.toString();
	}

	public int getMagic()
	{
		return magic.get();
	}	
}

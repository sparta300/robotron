package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Unset;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.structure.AbstractStructure;

/**
 * main store information, written once per store in the root block.
 * 
 * @author C006011
 */
public class StoreMain extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(StoreMain.class);

	private final LongPointer manifestAddress;

	public StoreMain(ByteBuffer bb)
	{
		this(bb, false);
	}

	private StoreMain(ByteBuffer bb, boolean reset)
	{
		super(bb);
		manifestAddress = new LongPointer(bb);
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
	 
	public static StoreMain forge(ByteBuffer bb)
	{
		return new StoreMain(bb, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.putLong((long)Unset.ADDRESS);
	}

	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		final long id = manifestAddress.get();
		sb.append("manifest=").append(id == Unset.ADDRESS ? "?" : id);	
		return sb.toString();
	}

	public long getManifestAddress()
	{
		return manifestAddress.get();
	}
	
	public void setManifestAddress(long address)
	{
		manifestAddress.set(address);
	}	
}

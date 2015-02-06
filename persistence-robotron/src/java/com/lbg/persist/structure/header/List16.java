package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.structure.raw.Header;

/**
 * shows that we have a list whose size is smaller than the maximum integer you can hold in 16 bits.
 * Currently doesn't actually hold any data.
 * 
 * @author C006011
 */
public class List16 extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(List16.class);

	public List16(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private List16(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);

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
	
	public static List16 forge(ByteBuffer bb, Header header)
	{
		return new List16(bb, header, true);
	}

	public void reset()
	{
		moveToStart();
	}
}

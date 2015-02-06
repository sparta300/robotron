package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.structure.raw.Header;

/**
 * a list of addresses, which are essentially pointers to where the real data is held.
 * 
 * @author C006011
 */
public class AddressList extends DoubleList
{
	private static final Logger log = LoggerFactory.getLogger(AddressList.class);
	
	public AddressList(ByteBuffer bb, Header header)
	{
		super(bb, header);
	}
	
	private AddressList(ByteBuffer bb, Header header, boolean reset)
	{
		this(bb, header);
		
		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(toString());
		}
	}

	public static AddressList forge(ByteBuffer bb, Header header)
	{
		return new AddressList(bb, header, true);
	}
}

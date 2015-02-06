package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.StringCodec;
import com.lbg.persist.structure.raw.Header;

/**
 * raw string data.
 * 
 * @author C006011
 */
public class StringData extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(StringData.class);
	
	private final String data;

	public StringData(ByteBuffer bb, Header header, StringCodec codec) throws PersistenceException
	{
		this(bb, header, codec, false);
	}
		
	private StringData(ByteBuffer bb, Header header, StringCodec codec, boolean reset) throws PersistenceException
	{
		super(bb, header);
		final int length = header.getDataSize();		
		final int savedLimit = bb.limit();
		final int startPos = bb.position();
		bb.limit(startPos + length);
		final ByteBuffer sliceBuffer = bb.slice();
		data = codec.decode(sliceBuffer);
		bb.limit(savedLimit);
		bb.position(startPos + length);
		markEnd();
		
		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(data);
		}
	}
	
	public static StringData forge(ByteBuffer bb, Header header, StringCodec codec) throws PersistenceException
	{
		return new StringData(bb, header, codec, true);
	}

	@Override
	public void reset()
	{
		moveToStart();
	}

	@Override
	public String toString()
	{
		return data;
	}	
}

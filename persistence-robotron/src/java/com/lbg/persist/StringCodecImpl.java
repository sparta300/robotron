package com.lbg.persist;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import javax.inject.Inject;

import com.mfdev.utility.PropertyMap;

/**
 * a codec for strings.
 * It currently assumes that all strings are using the same character set.
 * The character set is one of the standard ones such as <code>UTF-16</code> or <code>ISO-8859-1</code>
 * 
 * @author C006011
 */
public class StringCodecImpl implements StringCodec
{
	private final Charset characterSet;
	private final CharsetEncoder encoder;
	private final CharsetDecoder decoder;
	
	@Inject
	private StringCodecImpl(PropertyMap props)
	{
		characterSet = Charset.forName(props.getString("string.character.set").get());
		encoder = characterSet.newEncoder();
		decoder = characterSet.newDecoder();
	}
	
	@Override
	public ByteBuffer encode(String data) throws PersistenceException
	{
		final CharBuffer cb = CharBuffer.wrap(data);
		
		try
		{
			return encoder.encode(cb);
		}
		catch (CharacterCodingException cce)
		{
			throw new PersistenceException("string encoder failed", cce);
		}			
	}
	
	@Override
	public String decode(ByteBuffer data) throws PersistenceException
	{
		try
		{
			return decoder.decode(data).toString();
		}
		catch (CharacterCodingException cce)
		{
			throw new PersistenceException("string decoder failed", cce);
		}
	}
}

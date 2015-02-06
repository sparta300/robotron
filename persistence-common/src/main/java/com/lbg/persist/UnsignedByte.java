package com.lbg.persist;

/**
 * helps with unsigned bytes. 
 * 
 * @author C006011
 */
public class UnsignedByte
{
	public static final int MIN_VALUE = 0;
	public static final int MAX_VALUE = 255;
	
	private final int intValue;
	private final byte value;
	
	public UnsignedByte(int value)
	{
		if (value < MIN_VALUE || value > MAX_VALUE)
		{
			throw new IllegalArgumentException("long value out of unsigned byte range: " + value);
		}
		
		this.intValue = value;
		this.value = (byte)(value & 0xFF);
	}
	
	public UnsignedByte(long value)
	{
		if (value < MIN_VALUE || value > MAX_VALUE)
		{
			throw new IllegalArgumentException("long value out of unsigned byte range: " + value);
		}

		this.intValue = (int)value;
		this.value = (byte)(value & 0xFF);
	}
	
	public int asInt()
	{
		return intValue;
	}
	
	public byte asByte()
	{
		return value;
	}
	
	@Override	
	public String toString()
	{
		return String.valueOf(intValue);
	}	
	
	public static byte fromInt(int value)
	{		
		if (value < MIN_VALUE || value > MAX_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from int to unsigned byte value '" + value + "' exceeds unsigned byte maximum " + MAX_VALUE);
		}
				
		return (byte)(value & 0xFF);
	}
	
	public static byte fromLong(long value)
	{
		if (value < MIN_VALUE || value > MAX_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from long to unsigned byte value '" + value + "' exceeds unsigned byte maximum " + MAX_VALUE);
		}
		
		return (byte)(value & 0xFF);
	}
}

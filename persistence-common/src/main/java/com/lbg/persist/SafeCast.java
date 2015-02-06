package com.lbg.persist;


/**
 * allows conversion of number types from wider to narrower as long as the
 * actual value is within range.
 * 
 * @author C006011
 */
public class SafeCast
{
	public static final int UNSIGNED_SHORT_MIN = 0;
	public static final int UNSIGNED_SHORT_MAX = 65535;
	
	public static int fromLongToInt(long value)
	{
		if (value > Integer.MAX_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from long to int value '" + value + "' exceeds integer minimum " + Integer.MIN_VALUE);
		}
		else if (value < Integer.MIN_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from long to int value '" + value + "' more negative than integer minimum " + Integer.MIN_VALUE);			
		}

		return (int) value;
	}	

	public static byte fromIntToByte(int value)
	{
		if (value > Byte.MAX_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from int to byte value '" + value + "' exceeds signed byte maximum " + Byte.MAX_VALUE);
		}
		else if (value < Byte.MIN_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from int to byte value '" + value + "' more negative than signed byte minimum " + Byte.MIN_VALUE);
		}

		return (byte) value;
	}

	public static short fromIntToShort(int value)
	{
		if (value > Short.MAX_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from int to short value '" + value + "' exceeds short maximum " + Short.MAX_VALUE);
		}
		else if (value < Short.MIN_VALUE)
		{
			throw new IllegalArgumentException("cannot cast safely from int to short value '" + value + "' more negative than short minimum " + Short.MIN_VALUE);
		}

		return (short) value;
	}

	public static short fromIntToUnsignedShort(int value)
	{
		if (value > UNSIGNED_SHORT_MAX)
		{
			throw new IllegalArgumentException("cannot cast safely from int to unsigned short value '" + value + "' exceeds unsigned short maximum " + UNSIGNED_SHORT_MAX);
		}
		else if (value < UNSIGNED_SHORT_MIN)
		{
			throw new IllegalArgumentException("cannot cast safely from int to unsigned short value '" + value + "' cannot be negative " + UNSIGNED_SHORT_MAX);
		}

		return (short) value;
	}
	
	public static byte fromIntToUnsignedByte(int value)
	{
		//return UnsignedByte.fromInt(value);
        if (value > UnsignedByte.MAX_VALUE)
        {
            throw new IllegalArgumentException("cannot cast safely from int to unsigned byte value '" + value + "' exceeds unsigned byte maximum " + UnsignedByte.MAX_VALUE);
        }
        else if (value < 0)
        {
        	throw new IllegalArgumentException("cannot cast safely from int to unsigned byte value '" + value + "' cannot be negative");        	
        }
        
        if (value >= 0 && value <= Byte.MAX_VALUE)
        {
        	return (byte)value;	
        }

        value -= (UnsignedByte.MAX_VALUE + 1);
        return (byte) value;
	}
	
	public static int fromUnsignedByteToInt(byte value)
	{
        if (value >= 0 && value <= Byte.MAX_VALUE)
        {
        	return value;
        }
		else
		{
			return ((int)value) + UnsignedByte.MAX_VALUE + 1;
		}
	}

	public static byte fromLongToUnsignedByte(long value)
	{
		//return UnsignedByte.fromLong(value);
        if (value > UnsignedByte.MAX_VALUE)
        {
            throw new IllegalArgumentException("cannot cast safely from long to unsigned byte value '" + value + "' exceeds unsigned byte maximum " + UnsignedByte.MAX_VALUE);
        }
        
        return (byte)value;		
	}
}

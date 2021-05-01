package com.lbg.utility;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.lbg.persist.SafeCast;
import com.lbg.persist.UnsignedByte;
import com.mfdev.utility.BaseUtils;

public class SafeCastTest
{
	@Test
	public void testIntToByteZero()
	{
		assertEquals(0, SafeCast.fromIntToByte(0));
	}

	@Test
	public void testIntToByteMaxPositive()
	{
		assertEquals(Byte.MAX_VALUE, SafeCast.fromIntToByte(127));
	}

	@Test
	public void testIntToByteMinNegative()
	{
		assertEquals(Byte.MIN_VALUE, SafeCast.fromIntToByte(-128));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntToByteGreaterThanMax()
	{
		SafeCast.fromIntToByte(128);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIntToByteLessThanMin()
	{
		SafeCast.fromIntToByte(-129);
	}

	@Test
	public void sanityByteShift()
	{
		assertEquals(1, SafeCast.fromIntToByte(1 << 0));
		assertEquals(2, SafeCast.fromIntToByte(1 << 1));
		assertEquals(4, SafeCast.fromIntToByte(1 << 2));
		assertEquals(8, SafeCast.fromIntToByte(1 << 3));
		assertEquals(16, SafeCast.fromIntToByte(1 << 4));
		assertEquals(32, SafeCast.fromIntToByte(1 << 5));
		assertEquals(64, SafeCast.fromIntToByte(1 << 6));
		assertEquals(-128, SafeCast.fromIntToUnsignedByte(1 << 7));
	}

	@Test
	public void sanityIntShift()
	{
		assertEquals(1, 1 << 0);
		assertEquals(2, 1 << 1);
		assertEquals(4, 1 << 2);
		assertEquals(8, 1 << 3);
		assertEquals(16, 1 << 4);
		assertEquals(32, 1 << 5);
		assertEquals(64, 1 << 6);
		assertEquals(128, 1 << 7);
		assertEquals(256, 1 << 8);
		assertEquals(512, 1 << 9);
		assertEquals(1024, 1 << 10);
		assertEquals(2048, 1 << 11);
		assertEquals(4096, 1 << 12);
		assertEquals(8192, 1 << 13);
		assertEquals(16384, 1 << 14);
		assertEquals(32768, 1 << 15);
		assertEquals(65536, 1 << 16);
		assertEquals(131072, 1 << 17);
		assertEquals(262144, 1 << 18);
		assertEquals(524288, 1 << 19);
		assertEquals(1048576, 1 << 20);
		assertEquals(2097152, 1 << 21);
		assertEquals(4194304, 1 << 22);
		assertEquals(8388608, 1 << 23);
		assertEquals(16777216, 1 << 24);
		assertEquals(33554432, 1 << 25);
		assertEquals(67108864, 1 << 26);
		assertEquals(134217728, 1 << 27);
		assertEquals(268435456, 1 << 28);
		assertEquals(536870912, 1 << 29);
		assertEquals(1073741824, 1 << 30);
		assertEquals(Integer.MIN_VALUE, 1 << 31);
	}

	@Test
	public void sanityCast()
	{
		byte min = -128;
		byte max = 127;
		assertEquals(min, Byte.MIN_VALUE);
		assertEquals(max, Byte.MAX_VALUE);
		assertEquals((byte) 1, 1 << 0);
		assertEquals((byte) 2, 1 << 1);
		assertEquals((byte) 4, 1 << 2);
		assertEquals((byte) 8, 1 << 3);
		assertEquals((byte) 16, 1 << 4);
		assertEquals((byte) 32, 1 << 5);
		assertEquals((byte) 64, 1 << 6);
	}
	
	@Test
	public void sanityCastSignedMaximum()
	{
		int positive = 127;
		assertEquals("7f", Integer.toHexString(positive));
		assertEquals("1111111", Integer.toBinaryString(positive));
		byte positiveByte = (byte) positive;
		assertEquals("7f", BaseUtils.toHexString(positiveByte));
		
		assertEquals(positiveByte, SafeCast.fromIntToByte(positive));
	}
	
	@Test
	public void sanityCastUnsignedMaximum()
	{
		int positive = 255;
		assertEquals("ff", Integer.toHexString(positive));
		assertEquals("11111111", Integer.toBinaryString(positive));
		
		for (int i = 0; i <= Byte.MAX_VALUE; i++)
		{
			assertUnsignedByteRoundTrip(i);
		}
	}	
	
	/**
	 * unsigned numbers between 0 and 127 pass through unchanged.
	 */
	@Test
	public void roundTripPositive0to127()
	{
		for (int i = 0; i <= Byte.MAX_VALUE; i++)
		{
			assertUnsignedByteRoundTrip(i);
		}
	}	
	
	/**
	 * unsigned numbers between 128 and 255 are converted to negative numbers.
	 */	
	@Test
	public void roundTripPositive128to255()
	{
		int max = UnsignedByte.MAX_VALUE;
		
		for (int i = Byte.MAX_VALUE; i <= max; i++)
		{
			assertUnsignedByteRoundTrip(i);
		}
	}		
	
	private void assertUnsignedByteRoundTrip(int value)
	{
		byte b = SafeCast.fromIntToUnsignedByte(value);
		assertEquals(value, SafeCast.fromUnsignedByteToInt(b));
	}
}

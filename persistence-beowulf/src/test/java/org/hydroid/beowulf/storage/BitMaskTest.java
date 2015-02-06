package org.hydroid.beowulf.storage;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

public class BitMaskTest {
	@Test
	public void byte0() {
		BitMask mask = new BitMask((byte)0);
		assertEquals("00000000", mask.toString());
	}
	
	@Test
	public void byte1() {
		BitMask mask = new BitMask((byte)1);
		assertEquals("00000001", mask.toString());
	}	
	
	@Test
	public void byte2() {
		BitMask mask = new BitMask((byte)2);
		assertEquals("00000010", mask.toString());
	}
	
	@Test
	public void byte4() {
		BitMask mask = new BitMask((byte)4);
		assertEquals("00000100", mask.toString());
	}
	
	@Test
	public void byte8() {
		BitMask mask = new BitMask((byte)8);
		assertEquals("00001000", mask.toString());
	}	
	
	@Test
	public void byte16() {
		BitMask mask = new BitMask((byte)16);
		assertEquals("00010000", mask.toString());
	}	
	
	@Test
	public void byte32() {
		BitMask mask = new BitMask((byte)32);
		assertEquals("00100000", mask.toString());
	}	
	
	@Test
	public void byte64() {
		BitMask mask = new BitMask((byte)64);
		assertEquals("01000000", mask.toString());
	}	
	
	@Test
	public void byte127() {
		BitMask mask = new BitMask((byte)127);
		assertEquals("01111111", mask.toString());
	}	
	
	@Test
	public void byteNegative128() {
		BitMask mask = new BitMask((byte)-128);
		assertEquals("10000000", mask.toString());
	}	
	
	@Test
	public void byteNegative127() {
		BitMask mask = new BitMask((byte)-127);
		assertEquals("10000001", mask.toString());
	}
	
	@Test
	public void byteNegative1() {
		BitMask mask = new BitMask((byte)-1);
		assertEquals("11111111", mask.toString());
	}	
	
	@Test
	public void byteNegative2() {
		BitMask mask = new BitMask((byte)-2);
		assertEquals("11111110", mask.toString());
	}
	
	@Test
	public void byteNegative3() {
		BitMask mask = new BitMask((byte)-3);
		assertEquals("11111101", mask.toString());
	}	
	
	@Test
	public void short32767() {
		BitMask mask = new BitMask((short)Short.MAX_VALUE);
		assertEquals("01111111 11111111", mask.toString());
	}
	
	@Test
	public void shortNegative1() {
		BitMask mask = new BitMask((short)-1);
		assertEquals("11111111 11111111", mask.toString());
	}		
	
	@Test
	public void integer2147483647() {
		BitMask mask = new BitMask(Integer.MAX_VALUE);
		assertEquals("01111111 11111111 11111111 11111111", mask.toString());
	}
	
	@Test
	public void integerNegative1() {
		BitMask mask = new BitMask(-1);
		assertEquals("11111111 11111111 11111111 11111111", mask.toString());
	}

}

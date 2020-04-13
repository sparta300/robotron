package org.hydroid.beowulf.storage;

import static junit.framework.Assert.assertEquals;

import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Block56Slot4Subslot4Test {


	@Before
	public void create() {
		factory = new Block56Slot4Subslot4();
	}
	
	@Test
	public void encodeSubslot() {
		Locator subslot1 = factory.make(0, 0, 5);
		assertAddress(0L, 0, 5, subslot1);		
	}
	
	@Test
	public void encodeSlot() {
		Locator subslot1 = factory.make(0, 5, 0);
		assertAddress(0L, 5, 0, subslot1);		
	}	
	
	@Test
	public void encodeBlock() {
		Locator subslot1 = factory.make(5, 0, 0);
		assertAddress(5L, 0, 0, subslot1);		
	}
	
	@Test
	public void asAddressBlockSlotSubslot() {
		Locator locator = factory.make(1, 2, 3);
		assertEquals("b1s2x3", locator.asAddress());		
	}
	
	@Test
	public void asAddressBlockSlot() {
		Locator locator = factory.make(1, 2);
		assertEquals("b1s2", locator.asAddress());		
	}	
	
	@Test
	public void asAddressBlockZero() {
		Locator locator = factory.make(0, 2, 3);
		assertEquals("b0s2x3", locator.asAddress());		
	}	
	
	@Test
	public void asAddressSlotZero() {
		Locator locator = factory.make(1, 0, 3);
		assertEquals("b1s0x3", locator.asAddress());		
	}	
	
	@Test
	public void asAddressSubslotZero() {
		Locator locator = factory.make(1, 2, 0);
		assertEquals("b1s2x0", locator.asAddress());		
	}	
	
	@Test
	public void sanityCheckBlockMax() {
		// once it has been shifted eight bits to the right, this should be the maximum unsigned value
        byte[] data = new byte[8];
        data[7] = (byte)0xff;        
        data[6] = (byte)0xff;
        data[5] = (byte)0xff;
        data[4] = (byte)0xff;
        data[3] = (byte)0xff;
        data[2] = (byte)0xff;
        data[1] = (byte)0xff;
        data[0] = (byte)0x00;
        ByteBuffer bb = ByteBuffer.wrap(data);
        long max = bb.getLong();
        
        // in decimal, this is the ginormous number 72 057 594 037 927 935
        assertEquals(72057594037927935L, max);
        assertBinary("00000000 11111111 11111111 11111111 11111111 11111111 11111111 11111111", max);
        
        // the factory should then shift that pattern 8 bits to the left
        long locator = factory.make(max, 0).asLong();
        assertBinary("11111111 11111111 11111111 11111111 11111111 11111111 11111111 00000000", locator);        
	}
	
	@Test
	public void asAddressBlockMaximum() {
		Locator locator = factory.make(72057594037927935L, 0);
		assertBinary("11111111 11111111 11111111 11111111 11111111 11111111 11111111 00000000", locator.asLong());
		assertEquals("b72057594037927935s0", locator.asAddress());	
	}	
	
	@Test
	public void asAddressZeroSlotMaximum() {
		Locator locator = factory.make(1, 15, 3);
		assertEquals("b1s15x3", locator.asAddress());		
	}	
	
	@Test
	public void asAddressZeroSubslotMaximum() {
		Locator locator = factory.make(1, 2, 7);
		assertEquals("b1s2x7", locator.asAddress());		
	}	
	
	/**
	 * block ID check 2^0
	 */
	@Test
	public void asAddressBlock1() {
		Locator locator = factory.make(1, 0);
		
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000001 00000000", locator.asLong());		
	}
	
	/**
	 * block ID check 2^7
	 */
	@Test
	public void asAddressBlock128() {
		Locator locator = factory.make(128L, 0);
		
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 10000000 00000000", locator.asLong());		
	}
	
	/**
	 * block ID check 2^15
	 */
	@Test
	public void asAddressBlock32768() {
		Locator locator = factory.make(32768L, 0);
		
		assertBinary("00000000 00000000 00000000 00000000 00000000 10000000 00000000 00000000", locator.asLong());		
	}	
	
	/**
	 * block ID check 2^23
	 */
	@Test
	public void asAddressBlock8388608() {
		Locator locator = factory.make(8388608L, 0);
		
		assertBinary("00000000 00000000 00000000 00000000 10000000 00000000 00000000 00000000", locator.asLong());		
	}	
	
	/**
	 * block ID check 2^31
	 */
	@Test
	public void asAddressBlock2147483648() {
		Locator locator = factory.make(2147483648L, 0);
		
		assertBinary("00000000 00000000 00000000 10000000 00000000 00000000 00000000 00000000", locator.asLong());		
	}	
	
	/**
	 * block ID check 2^39
	 */
	@Test
	public void asAddressBlock549755813888() {
		Locator locator = factory.make(549755813888L, 0);
		
		assertBinary("00000000 00000000 10000000 00000000 00000000 00000000 00000000 00000000", locator.asLong());		
	}	
	
	/**
	 * block ID check 2^47
	 */
	@Test
	public void asAddressBlock140737488355328() {
		Locator locator = factory.make(140737488355328L, 0);
		
		assertBinary("00000000 10000000 00000000 00000000 00000000 00000000 00000000 00000000", locator.asLong());		
	}		
	
	/**
	 * block ID check 2^55
	 */
	@Test
	public void asAddressBlock36028797018963968() {
		Locator locator = factory.make(36028797018963968L, 0);
		
		assertBinary("10000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000", locator.asLong());		
	}
	
	@Test
	public void asAddressSlotMaximumBinary() {
		Locator locator = factory.make(0, 15);
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000000 11110000", locator.asLong());		
	}	
	
	@Test
	public void asAddressSubslotMaximumBinary() {
		Locator locator = factory.make(0, 0, 7);
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000000 00001111", locator.asLong());		
	}		
	
	@Test 
	public void sanityCheckBlockMask() {
		long mask = factory.describe().getBlockMask(true);
		assertBinary("11111111 11111111 11111111 11111111 11111111 11111111 11111111 00000000", mask);
	}
	
	@Test 
	public void sanityCheckSlotMask() {
		long mask = factory.describe().getSlotMask(true);
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000000 11110000", mask);
	}
	
	@Test 
	public void sanityCheckSubslotMask() {
		long mask = factory.describe().getSubslotMask(true);
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000000 00001110", mask);
	}	
	
	@Test 
	public void sanityCheckBlockMaskInverse() {
		long mask = factory.describe().getBlockMask(false);
		assertBinary("00000000 00000000 00000000 00000000 00000000 00000000 00000000 11111111", mask);
	}
	
	@Test 
	public void sanityCheckSlotMaskInverse() {
		long mask = factory.describe().getSlotMask(false);
		assertBinary("11111111 11111111 11111111 11111111 11111111 11111111 11111111 00001111", mask);
	}
	
	@Test 
	public void sanityCheckSubslotMaskInverse() {
		long mask = factory.describe().getSubslotMask(false);
		assertBinary("11111111 11111111 11111111 11111111 11111111 11111111 11111111 11110001", mask);
	}	
	
	private void assertBinary(String expectedBinary, long locator) {
		byte[] data = new byte[8];
		ByteBuffer bb = ByteBuffer.wrap(data);
		bb.putLong(locator);
		bb.flip();
		LocatorImpl wrapper = new LocatorImpl(bb);
		assertEquals(expectedBinary, wrapper.toBinaryString());
	}
	
	private void assertAddress(long blockId, int slotId, int subslotId, Locator locator) {
		assertEquals(blockId, locator.getBlockId());
		assertEquals(slotId, locator.getStructureId());
		assertEquals(subslotId, locator.getIndex());
	}

	@After
	public void destroy() {
		factory = null;
	}
	 
	private Block56Slot4Subslot4 factory;	
}

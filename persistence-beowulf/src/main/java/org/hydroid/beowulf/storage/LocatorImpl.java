/* --Developer Supplied Information--
 *
 * Copyright (c) 2004 Queen's Printer and Controller of HMSO.
 * All Rights Reserved.
 *
 * Environment:
 * JDK 1.4
 * EJB 2.0
 * Oracle 9iAS
 * 
 * --Source Control Details
 * $Date: 2004/05/10 17:35:03 $
 * $Workfile:$
 * $Revision: 1.1.1.1 $
 *
 */
package org.hydroid.beowulf.storage;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.ByteBuffer;

import org.hydroid.beowulf.BeowulfConstants;

import com.lbg.persist.SafeCast;

/**
 * treats a locator as a bit mask, allowing us to encode block/slot/subslot all into the same number.
 * The encoding is (msb first):<br>
 * <ol>
 *   <li>1 byte  - subslot</li>
 *   <li>6 bytes - block</li>
 *   <li>1 byte  - slot</li>   
 * </ol>
 * @author David Brown
 */
public class LocatorImpl implements Locator {
	public LocatorImpl(ByteBuffer bb) {
		this.bb = bb;
		this.encodedValue = bb.asLongBuffer().get();
	}

	public LocatorImpl(long encodedValue) {
		this.encodedValue = encodedValue;
	}

	public LocatorImpl(long blockId, int slotId) {
		this(blockId, slotId, 0);
	}

	public LocatorImpl(long blockId, int slotId, int subslotId) {
		if (blockId > MAX_BLOCK_ID) {
			throw new IllegalArgumentException("blockId must be less than or equal to " + MAX_BLOCK_ID);
		}

		byte b1 = (byte) ((blockId & 0xff0000000000L) >> 40);
		byte b2 = (byte) ((blockId & 0x00ff00000000L) >> 32);
		byte b3 = (byte) ((blockId & 0x0000ff000000L) >> 24);
		byte b4 = (byte) ((blockId & 0x000000ff0000L) >> 16);
		byte b5 = (byte) ((blockId & 0x00000000ff00L) >> 8);
		byte b6 = (byte) ((blockId & 0x0000000000ffL));
		this.bb = ByteBuffer.allocateDirect(SIZE_OF_LONG);
		bb.put((byte) subslotId);
		bb.put(b1);
		bb.put(b2);
		bb.put(b3);
		bb.put(b4);
		bb.put(b5);
		bb.put(b6);
		bb.put((byte) slotId);

		bb.flip();
		this.encodedValue = bb.asLongBuffer().get();
	}

	public String toBinaryString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		printOn(pw);
		pw.close();
		return sw.toString();
	}

	public static String toBinaryString(ByteBuffer bb) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		toBinary(bb, pw);
		pw.close();
		return sw.toString();
	}

	public String toString() {
		return String.valueOf(this.encodedValue);
	}
	
	
	public String asAddress() {
		if (encodedValue == BeowulfConstants.UNSET_LOCATOR) {
			return "UNSET";
		}
		
		final StringBuilder sb = new StringBuilder();
		sb.append("b").append(getBlockId());
		sb.append("s").append(getStructureId());
		
		if (getIndex() != 0) {
			sb.append("x").append(getIndex());
		}
				
		return sb.toString();
	}		


	public long asLong() {
		return encodedValue;
	}

	public long getBlockId() {
		return (encodedValue & BLOCK_MASK) >> BLOCK_SHIFT_BITS;
	}

	public int getStructureId() {
		return SafeCast.fromLongToInt((encodedValue & SLOT_MASK) >> SLOT_SHIFT_BITS);
	}

	public int getIndex() {
		return SafeCast.fromLongToInt((encodedValue & SUBSLOT_MASK) >> SUBSLOT_SHIFT_BITS);
	}

	private void printOn(PrintWriter pw) {
		toBinary(this.bb, pw);
	}

	static public void toBinary(ByteBuffer bb, PrintWriter pw) {
		// bb.flip();
		int byteCount = bb.capacity();
		int remaining = byteCount;

		for (int b = 0; b < byteCount; b++, remaining--) {
			int nibble = bb.get();

			for (int bit = 7; bit > -1; bit--) {
				if ((nibble & (1 << bit)) > 0) {
					pw.print('1');
				} else {
					pw.print('0');
				}
			}

			if (remaining != 1) {
				pw.print(' ');
			}
		}

	}

//	static public long toLong(long blockId, int slotId) {
//		return new LocatorImpl(blockId, slotId).asLocator();
//	}
//
//	static public long toLong(long blockId, int slotId, int subslotId) {
//		return new LocatorImpl(blockId, slotId, subslotId).asLocator();
//	}

	private static final long BLOCK_MASK = 0x00ffffffffffff00L;
	private static final long SLOT_MASK = 0x00000000000000ffL;
	private static final long SUBSLOT_MASK = 0xff00000000000000L;

	private static final int BLOCK_SHIFT_BITS = 8;
	private static final int SLOT_SHIFT_BITS = 0;
	private static final int SUBSLOT_SHIFT_BITS = 56;

	private static int SIZE_OF_LONG = 8;
	private ByteBuffer bb;
	private long encodedValue;
	static final long MAX_BLOCK_ID = 281474976710655L;

}

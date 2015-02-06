package org.hydroid.beowulf.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lbg.persist.SafeCast;

public class BitMask {
	public BitMask(byte bits) {
		bitCount = 8;
		asString = Integer.toBinaryString(bits);
		int length = asString.length();
		
		
		if (length < 8) {
			int pad = 8 - length;
			StringBuilder sb = new StringBuilder(8);
			
			for (int p = 0; p < pad; p++) {
				sb.append('0');
			}
			
			sb.append(asString);
			
			asString = sb.toString();
		}
		
//		booleans = new ArrayList<Boolean>(bitCount);
//		nibble(bits);
	}
	
	public BitMask(short bits) {
		bitCount = 16;
		asString = Integer.toBinaryString(bits).substring(34);
//		booleans = new ArrayList<Boolean>(bitCount);
//		byte nibble1 = SafeCast.fromShortToUnsignedByte(bits & 0xff00 >>> 8);
//		byte nibble2 = SafeCast.fromShortToUnsignedByte(bits & 0x00ff);
//		nibble(nibble1);
//		nibble(nibble2);
	}
	
	public BitMask(int bits) {
		bitCount = 32;
		booleans = new ArrayList<Boolean>(bitCount);
		byte nibble1 = SafeCast.fromIntToUnsignedByte(bits & 0xff000000 >>> 24);
		byte nibble2 = SafeCast.fromIntToUnsignedByte(bits & 0x00ff0000 >>> 16);
		byte nibble3 = SafeCast.fromIntToUnsignedByte(bits & 0x0000ff00 >>> 8);
		byte nibble4 = SafeCast.fromIntToUnsignedByte(bits & 0x000000ff);		
		nibble(nibble1);
		nibble(nibble2);
		nibble(nibble3);
		nibble(nibble4);
	}
	
	public BitMask(long bits) {
		bitCount = 64;
		booleans = new ArrayList<Boolean>(bitCount);
		byte nibble1 = SafeCast.fromLongToUnsignedByte(bits & 0xff00000000000000L >>> 56);
		byte nibble2 = SafeCast.fromLongToUnsignedByte(bits & 0x00ff000000000000L >>> 48);
		byte nibble3 = SafeCast.fromLongToUnsignedByte(bits & 0x0000ff0000000000L >>> 40);
		byte nibble4 = SafeCast.fromLongToUnsignedByte(bits & 0x000000ff00000000L >>> 32);	
		byte nibble5 = SafeCast.fromLongToUnsignedByte(bits & 0x00000000ff000000L >>> 24);
		byte nibble6 = SafeCast.fromLongToUnsignedByte(bits & 0x0000000000ff0000L >>> 16);
		byte nibble7 = SafeCast.fromLongToUnsignedByte(bits & 0x000000000000ff00L >>> 8);
		byte nibble8 = SafeCast.fromLongToUnsignedByte(bits & 0x00000000000000ffL);		
		nibble(nibble1);
		nibble(nibble2);
		nibble(nibble3);
		nibble(nibble4);		
		nibble(nibble5);
		nibble(nibble6);
		nibble(nibble7);
		nibble(nibble8);		
	}
		
	private void nibble(byte bits) { 
		Boolean[] booleans = new Boolean[8];
		int index = 7;
		int current = bits;
		
		for (int b = 0; b < 8; b++, index--) {
			int andResult = current & 1;
			booleans[index] = andResult == 1 ? true : false;
			current = current >>> 1;
		}
		
		this.booleans.addAll(Arrays.asList(booleans));
	}
	
	@Override
	public String toString() {
		return asString;
	}
	
	private String asString;
	private List<Boolean> booleans;
	private int bitCount;
}

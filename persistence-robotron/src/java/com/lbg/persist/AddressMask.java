package com.lbg.persist;

/**
 * an enum that represents how each part of the address is encoded into a
 * long, represented as a bit mask and a number of bit shifts to the right
 * to make its LSB the LSB of a long.
 * 
 * @author smiley
 *
 */
public enum AddressMask
{
	BLOCK      (0xffffffffffffff00L, 0x00000000000000ffL, 8), 
	SLOT       (0x00000000000000f0L, 0xffffffffffffff0fL, 4), 
	SUBSLOT    (0x000000000000000eL, 0xfffffffffffffff1L, 1), 
	HAS_SUBSLOT(0x0000000000000001L, 0xfffffffffffffffeL, 0);

	private final long mask;
	private final long antimask;
	private final int shift;
	
	private AddressMask(long mask, long antimask, int bitShiftCount)
	{
		this.mask = mask;
		this.antimask = antimask;
		this.shift = bitShiftCount;
	}

	public long decode(long value)
	{
		return (mask & value) >>> shift;
	}

	public long encode(long value)
	{
		return (value << shift) & mask;
	}

	public long getAntimask()
	{
		return antimask;
	}

	public long getMask()
	{
		return mask;
	}

}


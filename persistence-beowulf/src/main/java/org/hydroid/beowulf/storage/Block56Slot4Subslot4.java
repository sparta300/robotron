package org.hydroid.beowulf.storage;

import org.hydroid.beowulf.BeowulfConstants;

import com.lbg.persist.SafeCast;

public class Block56Slot4Subslot4 implements LocatorFactory {
	public Block56Slot4Subslot4() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public Locator make(long blockId, int slotId) {
		long encodedBlock = Mask.BLOCK.encode(blockId);
		long encodedSlot = Mask.SLOT.encode(slotId);
		long encodedSubslot = Mask.SUBSLOT.encode(0);
		long hasSubslot = Mask.HAS_SUBSLOT.encode(0);		
		return make(encodedBlock | encodedSlot | encodedSubslot | hasSubslot);
	}

	/**
	 * {@inheritDoc}
	 */
	public Locator make(long blockId, int slotId, int subslotId) {
		long encodedBlock = Mask.BLOCK.encode(blockId);
		long encodedSlot = Mask.SLOT.encode(slotId);
		long encodedSubslot = Mask.SUBSLOT.encode(subslotId);
		long hasSubslot = Mask.HAS_SUBSLOT.encode(1);
		return make (encodedBlock | encodedSlot | encodedSubslot | hasSubslot);
	}

	/**
	 * {@inheritDoc}
	 */
	public Locator make(final long locator) {
		return new Locator() {
			public String asAddress() {		
				if (locator == BeowulfConstants.UNSET_LOCATOR) {
					return "UNSET";
				}
				
				long encodedHasSubslot = Mask.HAS_SUBSLOT.decode(locator);
				
				if (encodedHasSubslot != 1) {
					return  String.format("b%ds%d", getBlockId(), getStructureId());
				}
				
				return String.format("b%ds%dx%s", getBlockId(), getStructureId(), getIndex());
			}

			public long asLong() {
				return locator;
			}

			public long getBlockId() {
				return Mask.BLOCK.decode(locator);
			}
			
			public int getStructureId() {
				return SafeCast.fromLongToInt(Mask.SLOT.decode(locator));
			}
			
			public int getIndex() {
				return SafeCast.fromLongToInt(Mask.SUBSLOT.decode(locator));
			}
			
		};
	}
	
	/**
	 * {@inheritDoc}
	 */	
	public LocatorFactoryDetails describe() {
		return DETAILS;
	}


	/**
	 * an enum that represents how each part of the address is encoded into a long, represented as a bit mask
	 * and a number of bit shifts to the right to make its LSB the LSB of a long.
	 * 
	 * @author smiley
	 *
	 */
	private enum Mask {
		BLOCK(      0xffffffffffffff00L, 0x00000000000000ffL, 8),
		SLOT(       0x00000000000000f0L, 0xffffffffffffff0fL, 4),
		SUBSLOT(    0x000000000000000eL, 0xfffffffffffffff1L, 1),
		HAS_SUBSLOT(0x0000000000000001L, 0xfffffffffffffffeL, 0);
		  
		private Mask(long mask, long antimask, int bitShiftCount) {
	    	this.mask = mask;
	    	this.antimask = antimask;
	    	this.shift = bitShiftCount;
	    }
		
		public long decode(long value) {
			return (mask & value) >>> shift; 
		}
		
		public long encode(long value) {
			return (value << shift) & mask; 
		}		
		
		public long getAntimask() {
			return antimask;
		}
		
		public long getMask() {
			return mask;
		}		
		
	    private long mask;
	    private long antimask;	
	    private int shift;
	}


	private static final LocatorFactoryDetails DETAILS = new LocatorFactoryDetails() {
		
		public long getSubslotMask(boolean isPositive) {
			return isPositive ? Mask.SUBSLOT.getMask() : Mask.SUBSLOT.getAntimask();
		}
		
		public long getSlotMask(boolean isPositive) {
			return isPositive ? Mask.SLOT.getMask() : Mask.SLOT.getAntimask();
		}
		
		public long getBlockMask(boolean isPositive) {
			return isPositive ? Mask.BLOCK.getMask() : Mask.BLOCK.getAntimask();
		}			
		
		public int getMaxSubslotId() {
			return 7;
		}
		
		public int getMaxSlotId() {
			return 15;
		}
		
		public long getMaxBlockId() {
			return 72057594037927935L;
		}		

	};

}

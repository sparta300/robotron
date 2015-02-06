package com.lbg.persist;


/**
 * an implementation of a swizzler that uses 14 bytes for the block ID and 4
 * bytes for the component and index IDs.
 * 
 * @author C006011
 */
public class SwizzlerImpl implements Swizzler
{
	public SwizzlerImpl()
	{
		super();
	}

	@Override
	public Address make(long blockId, int componentId)
	{
		long encodedBlock = AddressMask.BLOCK.encode(blockId);
		long encodedSlot = AddressMask.SLOT.encode(componentId);
		long encodedSubslot = AddressMask.SUBSLOT.encode(0);
		long hasSubslot = AddressMask.HAS_SUBSLOT.encode(0);
		return make(encodedBlock | encodedSlot | encodedSubslot | hasSubslot);
	}

	@Override
	public Address make(long blockId, int componentId, int subslotId)
	{
		long encodedBlock = AddressMask.BLOCK.encode(blockId);
		long encodedSlot = AddressMask.SLOT.encode(componentId);
		long encodedSubslot = AddressMask.SUBSLOT.encode(subslotId);
		long hasSubslot = AddressMask.HAS_SUBSLOT.encode(1);
		return make(encodedBlock | encodedSlot | encodedSubslot | hasSubslot);
	}

	@Override
	public Address make(final long locator)
	{
		return new Address()
		{
			public String asAddress()
			{
				if (locator == Unset.ADDRESS)
				{
					return "UNSET";
				}

				long encodedHasSubslot = AddressMask.HAS_SUBSLOT.decode(locator);

				if (encodedHasSubslot != 1)
				{
					return String.format("b%ds%d", getBlockId(), getStructureId());
				}

				return String.format("b%ds%dx%s", getBlockId(), getStructureId(), getIndex());
			}

			public long asLong()
			{
				return locator;
			}

			public long getBlockId()
			{
				return AddressMask.BLOCK.decode(locator);
			}

			public int getStructureId()
			{
				return SafeCast.fromLongToInt(AddressMask.SLOT.decode(locator));
			}

			public int getIndex()
			{
				return SafeCast.fromLongToInt(AddressMask.SUBSLOT.decode(locator));
			}

		};
	}
}

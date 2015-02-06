package org.hydroid.beowulf.storage;

public interface LocatorFactoryDetails
{
	long getBlockMask(boolean isPositive);

	long getSlotMask(boolean isPositive);

	long getSubslotMask(boolean isPositive);

	long getMaxBlockId();

	int getMaxSlotId();

	int getMaxSubslotId();
}

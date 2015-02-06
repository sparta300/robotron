package org.hydroid.beowulf.storage;

/**
 * a factory for making locators using a variety of different methods.
 * 
 * @author smiley
 */
public interface LocatorFactory
{
	Locator make(long blockId, int slotId);

	Locator make(long blockId, int slotId, int subslotId);

	Locator make(long locator);

	LocatorFactoryDetails describe();
}

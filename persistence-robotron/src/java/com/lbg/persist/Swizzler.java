package com.lbg.persist;


public interface Swizzler
{
	Address make(long blockId, int componentId);

	Address make(long blockId, int componentId, int indexId);

	Address make(long locator);
}

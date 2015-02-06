package com.lbg.persist;

public interface Address
{
	long getBlockId();

	int getStructureId();

	int getIndex();
	
	String asAddress();
	
	long asLong();
}

package com.lbg.persist;

public interface AddressImpl {

	long getBlockId();

	int getSlotId();

	int getSubslotId();
	
	String asAddress();
	
	long asLong();
	
	
}

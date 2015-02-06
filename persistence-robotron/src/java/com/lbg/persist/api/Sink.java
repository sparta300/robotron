package com.lbg.persist.api;

import com.lbg.resource.PhysicalResourceException;

public interface Sink<T>
{
	void put(T data) throws PhysicalResourceException;
}

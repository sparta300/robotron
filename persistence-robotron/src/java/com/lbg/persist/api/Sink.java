package com.lbg.persist.api;

import org.hydroid.file.PhysicalResourceException;

public interface Sink<T>
{
	void put(T data) throws PhysicalResourceException;
}

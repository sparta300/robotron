package com.lbg.persist.api;

import org.hydroid.file.PhysicalResourceException;

public interface Source<T>
{
	T get() throws PhysicalResourceException;
}

package com.lbg.persist.api;

import com.lbg.resource.PhysicalResourceException;

public interface Source<T>
{
	T get() throws PhysicalResourceException;
}

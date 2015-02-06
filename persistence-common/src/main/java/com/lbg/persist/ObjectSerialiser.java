package com.lbg.persist;

import java.io.Serializable;

import com.lbg.resource.PhysicalResourceException;

public interface ObjectSerialiser
{

	byte[] write(Serializable object) throws PhysicalResourceException;

	<T> T read(byte[] data) throws PhysicalResourceException;

	<T> T read(byte[] data, int offset, int length) throws PhysicalResourceException;

}

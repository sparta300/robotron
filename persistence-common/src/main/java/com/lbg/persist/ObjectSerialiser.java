package com.lbg.persist;

import java.io.Serializable;

import org.hydroid.file.PhysicalResourceException;

public interface ObjectSerialiser
{

	byte[] write(Serializable object) throws PhysicalResourceException;

	<T> T read(byte[] data) throws PhysicalResourceException;

	<T> T read(byte[] data, int offset, int length) throws PhysicalResourceException;

}

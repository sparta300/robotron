package com.lbg.persist;

import java.nio.ByteBuffer;

public interface StringCodec
{

	ByteBuffer encode(String data) throws PersistenceException;

	String decode(ByteBuffer data) throws PersistenceException;

}

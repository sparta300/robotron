package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.PersistenceException;

public interface BlockReader
{

	void readStructures(ByteBuffer bb, BlockBuilder builder) throws PersistenceException;

}

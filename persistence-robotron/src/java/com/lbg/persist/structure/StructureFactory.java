package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.structure.raw.Header;
import com.mfdev.utility.PropertyMap;

public interface StructureFactory
{
	Structure create(StructureType structureType, ByteBuffer bb);

	Structure createWithHeader(StructureType structureType,ByteBuffer bb, PropertyMap parameters) throws PersistenceException;

	Header createStopHeader(ByteBuffer bb);
}

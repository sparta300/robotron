package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.PersistenceException;
import com.mfdev.utility.PropertyMap;

public interface StructureLibrary
{

	HeaderStructureCommand getHeaderCommand(StructureType type);
	BasicStructureCommand getBasicCommand(StructureType type);
	
	Structure createList16(ByteBuffer bb, PropertyMap parameters) throws PersistenceException;
	
	Structure createList16(ByteBuffer bb, int elementCount,	StructureType managementType, StructureType elementType) throws PersistenceException;
	
	Structure createList16(ByteBuffer bb, int elementCount,	StructureType managementType, StructureType elementType, PropertyMap parameters) throws PersistenceException;

}

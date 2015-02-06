package com.lbg.persist.structure;

import java.nio.ByteBuffer;
import java.util.List;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.structure.raw.Header;

public interface StructureReader
{
	/**
	 * read a basic structure.
	 * A basic structure does not have a header and there are as few as possible of them, to help
	 * with compatibility.  These are  the most fundamental boot-strap types and should not be used 
	 * other than to read enough from a file to select a compatible reader. 
	 * 
	 * @param structureType
	 * @param bb a byte buffer
	 * @return the structure
	 */
	Structure readStructure(StructureType structureType, ByteBuffer bb) throws PersistenceException;

	/**
	 * read a complex structure.
	 * A complex structure is not necessarily complex itself but is handled differently because it has
	 * a header as a bit of preamble.
	 * 
	 * @param structureEnum
	 * @param header the header that immediately preceeds this structure
	 * @param bb a byte buffer
	 * @return the structure
	 */
	Structure readStructure(StructureType structureEnum, Header header, ByteBuffer bb) throws PersistenceException;

	/**
	 * read in a composite structure if you know what types you are expecting.
	 * 
	 * @param types a list of the expected types and the order they are expected to be read in
	 * @param header the header for the entire composite
	 * @param bb a byte buffer
	 * @return a list of structures
	 */
	List<Structure> readComposite(List<StructureType> types, Header header, ByteBuffer bb) throws PersistenceException;
}

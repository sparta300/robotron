package com.lbg.persist.engine;

import java.nio.ByteBuffer;

import com.lbg.persist.Address;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.structure.Block;
import com.lbg.persist.structure.Structure;
import com.lbg.resource.PhysicalResourceException;

/**
 * an interface for getting access to individual components in a repository file.
 * This is not a real translation lookaside buffer, since they are implemented in hardware using content-addressable memory but the idea is the same.
 * We want to avoid redoing a lot of work, especially mapping out a block full of structures, when we look up an individual component.
 * We use tasks to allow for asynchronous and multi-threaded execution.
 * 
 * @author C006011
 */
public interface TranslationLookasideBuffer
{
	void registerName(String name, int blockId, int componentId);
	
	void registerName(String name, Address address);
	
	void registerName(String name, long address);

	int forComponent(RepositoryFileHolder holder, LookasideTask task, AccessMode mode, String name) throws PhysicalResourceException, PersistenceException;
	
	int forComponent(RepositoryFileHolder holder, LookasideTask task, AccessMode mode,int blockId, int componentId) throws PhysicalResourceException, PersistenceException;

	Block readRootBlock(RepositoryFileHolder holder, ByteBuffer bb) throws PersistenceException;

	Block readBlock(int b, ByteBuffer blockBuffer) throws PersistenceException;

	void registerName(String name, Structure structure);


	 
}

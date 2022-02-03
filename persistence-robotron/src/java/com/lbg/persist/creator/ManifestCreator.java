package com.lbg.persist.creator;

import static com.lbg.persist.PersistConstants.ROOT_BLOCK_ID;
import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.STORE_MAIN;
import static com.lbg.persist.structure.StructureType.TELEMETRY_STREAM;
import static com.lbg.persist.structure.StructureType.VERSION;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import org.hydroid.file.PhysicalResourceException;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.BlockType;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.Swizzler;
import com.lbg.persist.daemon.ScratchBuffer;
import com.lbg.persist.daemon.ScratchBufferImpl;
import com.lbg.persist.structure.BlockPlanner;
import com.lbg.persist.structure.StructureFactory;
import com.lbg.persist.structure.StructureLibrary;
import com.lbg.persist.structure.raw.BlockMain;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.StoreMain;
import com.lbg.persist.structure.raw.VersionNumber;

/**
 * a file creator that adds a manifest.
 * 
 * @author C006011
 */
public class ManifestCreator implements FileCreator
{
	private static final Logger log = LoggerFactory.getLogger(ManifestCreator.class);
	
	private final PageDaemon pageDaemon;
	private final StructureFactory structureFactory;
	private final StructureLibrary structureLibrary;
	private final Swizzler swizzler;
	private final ScratchBuffer scratch = new ScratchBufferImpl(2048);
	
	@Inject
	private ManifestCreator(PageDaemon pageDaemon, StructureFactory structureFactory, StructureLibrary structureLibrary, Swizzler swizzler)
	{
		this.pageDaemon = pageDaemon;
		this.structureFactory = structureFactory;
		this.structureLibrary = structureLibrary;
		this.swizzler = swizzler;
	}
	
	@Override
	public void createFile(RepositoryFile file, int blockSize,	int maxBlockCount) throws PersistenceException
	{
		try
		{
			writeRootBlock(file, blockSize, maxBlockCount);
			
			writeNonRootBlocks(file, blockSize, maxBlockCount);
		}
		catch (PageException e)
		{
			log.error("page exception on page-in", e);
		}
		catch (PhysicalResourceException pre)
		{
			log.error("i/o exception on page-in", pre);
		}
		finally
		{
			pageDaemon.flushAll();
	
			try
			{
				file.close();
			}
			catch (IOException e)
			{
				log.error("i/o exception closing repo", e);
			}
		}	
	}

	private void writeRootBlock(RepositoryFile file, int blockSize, int maxBlockCount) throws PhysicalResourceException, PersistenceException
	{
		final PageIdentifier pageId = PageIdentifier.forRootBlock(file, blockSize);
		final Page page = pageDaemon.make(pageId);
		final ByteBuffer bb = page.getByteBuffer();
		
		// magic
		final Magic magic = (Magic) structureFactory.create(MAGIC, bb);
		log.info(magic.toString());
		
		// block main
		final BlockMain blockBasic = (BlockMain) structureFactory.create(BLOCK_MAIN, bb);
		blockBasic.setBlockId(0);
		blockBasic.setBlockType(BlockType.ROOT);
		
		// version
		final VersionNumber version = (VersionNumber) structureFactory.create(VERSION, bb);
		version.setMajor(1);
		version.setMinor(2);
		version.setBuild(3);
		version.setPatch(4);
		
		// geometry
		final Geometry geometry = (Geometry) structureFactory.create(GEOMETRY, bb);
		geometry.setBlockSize(blockSize);
		geometry.setMaxBlockCount(maxBlockCount);
		
		// store main
		final StoreMain storeMain = (StoreMain) structureFactory.create(STORE_MAIN, bb);
			
		// okay, now we have the components that are out in the open.  The component IDs start from 
		// zero here all the ones before this one are registered as named components and cannot be
		// accessed by component index
		
		// [0] telemetry stream
		final BlockPlanner planner = new BlockPlanner(structureLibrary, structureFactory, scratch, bb, blockSize);			
		planner.write(TELEMETRY_STREAM);			
		
		// [1] manifest
		int componentId = 0;
		final String manifestData = new ManifestBuilder().withContentType("telemetry-stream")
		                                                 .addService("telemetry-service")
		                                                 .addStructure("stream", swizzler.make(ROOT_BLOCK_ID, componentId).asLong())
		                                                 .build();  

		planner.writeString(manifestData);
		componentId++;
		
		// set the manifest address	in the main store structure	
		storeMain.setManifestAddress(swizzler.make(ROOT_BLOCK_ID, componentId).asLong());
		
		// [2] wilderness
		planner.createWilderness();
	}
	 
	/**
	 * write all of the non-root blocks that make up the rest of the file.
	 * Each block is simply filled with wilderness.
	 *  
	 * @param file
	 * @param blockSize
	 * @param maxBlockCount
	 * @throws PhysicalResourceException
	 * @throws PersistenceException
	 */
	private void writeNonRootBlocks(RepositoryFile file, int blockSize, int maxBlockCount) throws PhysicalResourceException, PersistenceException
	{
		// spin through all of the remaining blocks and fill them with wilderness
		for (int b = 1; b < maxBlockCount; b++)
		{
			final PageIdentifier pageId = PageIdentifier.forBlock(file, b, blockSize);
			final Page page = pageDaemon.make(pageId);
			final ByteBuffer pageByteBuffer = page.getByteBuffer();
			final BlockPlanner planner = new BlockPlanner(structureLibrary, structureFactory, scratch, pageByteBuffer, blockSize);
			
			final BlockMain blockBasic = (BlockMain) structureFactory.create(BLOCK_MAIN, pageByteBuffer);
			blockBasic.setBlockId(b);
			blockBasic.setBlockType(BlockType.NORMAL);
					
			planner.createWilderness();
		}
	}
}

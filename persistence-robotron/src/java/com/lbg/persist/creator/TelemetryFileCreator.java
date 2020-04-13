package com.lbg.persist.creator;

import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.LIST_BOOLEAN_8;
import static com.lbg.persist.structure.StructureType.LIST_TELEMETRY_FRAME;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.STORE_MAIN;
import static com.lbg.persist.structure.StructureType.TELEMETRY_STREAM;
import static com.lbg.persist.structure.StructureType.VERSION;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.BlockType;
import com.lbg.persist.PersistenceException;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;
import com.lbg.persist.daemon.ScratchBuffer;
import com.lbg.persist.daemon.ScratchBufferImpl;
import com.lbg.persist.main.Calculator;
import com.lbg.persist.structure.BlockPlanner;
import com.lbg.persist.structure.StructureFactory;
import com.lbg.persist.structure.StructureLibrary;
import com.lbg.persist.structure.raw.BlockMain;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.StoreMain;
import com.lbg.persist.structure.raw.VersionNumber;

public class TelemetryFileCreator implements FileCreator
{
	private static final Logger log = LoggerFactory.getLogger(TelemetryFileCreator.class);
	private final PageDaemon pageDaemon;
	private final StructureFactory structureFactory;
	private final StructureLibrary structureLibrary;
	
	private TelemetryFileCreator(PageDaemon pageDaemon, StructureFactory structureFactory, StructureLibrary structureLibrary)
	{
		this.pageDaemon = pageDaemon;
		this.structureFactory = structureFactory;
		this.structureLibrary = structureLibrary;
	}
	
	@Override
	public void createFile(RepositoryFile file, int blockSize,	int maxBlockCount) throws PersistenceException
	{
		final PageIdentifier pageId = new PageIdentifier(file, 0L, blockSize);

		try
		{
			final Page page = pageDaemon.make(pageId);
			final ByteBuffer bb = page.getByteBuffer();
			
			final Magic magic = (Magic) structureFactory.create(MAGIC, bb);
			log.info(magic.toString());
			
			final BlockMain blockBasic = (BlockMain) structureFactory.create(BLOCK_MAIN, bb);
			blockBasic.setBlockId(0);
			blockBasic.setBlockType(BlockType.ROOT);
			
			final VersionNumber version = (VersionNumber) structureFactory.create(VERSION, bb);
			version.setMajor(1);
			version.setMinor(2);
			version.setBuild(3);
			version.setPatch(4);
			
			// fill in the geometry
			final Geometry geometry = (Geometry) structureFactory.create(GEOMETRY, bb);
			geometry.setBlockSize(blockSize);
			geometry.setMaxBlockCount(maxBlockCount);

			final StoreMain storeMain = (StoreMain) structureFactory.create(STORE_MAIN, bb);
			
			// create a wilderness manager
			final ScratchBuffer scratchBuffer = new ScratchBufferImpl(2 * blockSize);
			final ByteBuffer scratch = scratchBuffer.getByteBuffer();
			final BlockPlanner wilderness = new BlockPlanner(structureLibrary, structureFactory, scratchBuffer, bb, blockSize);
			
			wilderness.write(TELEMETRY_STREAM);

			// now we can calculate the size of the longest list of telemetry frames possible 
			// within the given space left in the root block
			final Calculator telemetryFrameCalculator = wilderness.getList16Calculator(LIST_BOOLEAN_8, LIST_TELEMETRY_FRAME);
			final int frameCount = telemetryFrameCalculator.getElementCount(wilderness.getSpace());
			
			wilderness.grazeList16(frameCount, LIST_BOOLEAN_8, LIST_TELEMETRY_FRAME);
						
			wilderness.createWilderness();
		
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
	

}

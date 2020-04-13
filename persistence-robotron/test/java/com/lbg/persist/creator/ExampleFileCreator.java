package com.lbg.persist.creator;

import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.LIST_BOOLEAN_8;
import static com.lbg.persist.structure.StructureType.LIST_DOUBLE;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.STOP;
import static com.lbg.persist.structure.StructureType.VERSION;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;

import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.hydroid.file.RepositoryFile;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.SafeCast;
import com.lbg.persist.Swizzler;
import org.hydroid.page.Page;
import org.hydroid.page.PageException;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageIdentifier;
import com.lbg.persist.structure.ListHelper;
import com.lbg.persist.structure.StructureFactory;
import com.lbg.persist.structure.StructureLibrary;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Header;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.VersionNumber;
import com.lbg.utility.PropertyMap;

/**
 * create an empty file.
 * 
 * @author C006011
 */
public class ExampleFileCreator implements FileCreator
{
	private static final Logger log = LoggerFactory.getLogger(ExampleFileCreator.class);
	
	private final PageDaemon pageDaemon;
	private final StructureFactory structureFactory;
	private final StructureLibrary structureLibrary;
	private final ListHelper listHelper;
	
	@Inject
	private ExampleFileCreator(PageDaemon pageDaemon, StructureFactory structureFactory, StructureLibrary structureLibrary,
                               ListHelper listHelper, Swizzler swizzler, PropertyMap props)
	{
		this.pageDaemon = pageDaemon;
		this.listHelper = listHelper;
		this.structureFactory = structureFactory;
		this.structureLibrary = structureLibrary;
	}
		 
	@Override
	public void createFile(RepositoryFile file, int blockSize,	int maxBlockCount) throws PersistenceException
	{
		final PageIdentifier pageId = new PageIdentifier(file, 0, blockSize);

		try
		{
			final Page page = pageDaemon.make(pageId);
			final ByteBuffer bb = page.getByteBuffer();
			
			final Magic magic = (Magic) structureFactory.create(MAGIC, bb);
			log.info(magic.toString());
			
			final VersionNumber version = (VersionNumber) structureFactory.create(VERSION, bb);
			version.setMajor(1);
			version.setMinor(2);
			version.setBuild(3);
			version.setPatch(4);
			
			// fill in the geometry
			final Geometry geometry = (Geometry) structureFactory.create(GEOMETRY, bb);
			geometry.setBlockSize(blockSize);
			geometry.setMaxBlockCount(maxBlockCount);
			
			// calculate the maximum possible space we have left after writing all the mandatory header stuff
			final int savedPosition = bb.position();
			final int maxSpace = blockSize - savedPosition;
			
			// see how much space a minimal header with no data takes up.  We need to leave
			// this much space to create a STOP header to mark the end of the block
			structureFactory.create(HEADER, bb);
			final int afterHeader = bb.position();
			final int headerSize = afterHeader - savedPosition;
			final int stopHeaderSize = headerSize;
			final int wildernessHeaderSize = headerSize;
			final int maxWildernessSpace = maxSpace - (stopHeaderSize + wildernessHeaderSize);
			log.info("maxWildernessSpace=" + maxWildernessSpace);
			
			// go back to our saved position and write an empty list
			bb.position(savedPosition);			
			structureLibrary.createList16(bb, 0, LIST_BOOLEAN_8, LIST_DOUBLE);
			final int listPreambleSize = bb.position() - savedPosition;
			
			// go back again and see how much space a single element takes up
			bb.position(savedPosition);
			structureLibrary.createList16(bb, 1, LIST_BOOLEAN_8, LIST_DOUBLE);
			final int spacePerElement = bb.position() - (savedPosition + listPreambleSize);
			final int elementCount = listHelper.calculateSize(maxWildernessSpace, listPreambleSize, spacePerElement);
			
			// go back again and write the longest list possible within the given space
			bb.position(savedPosition);
			structureLibrary.createList16(bb, elementCount, LIST_BOOLEAN_8, LIST_DOUBLE);
			
			// now finish the block with a STOP header
			final Header stopHeader = (Header) structureFactory.create(HEADER, bb);
			stopHeader.setType(SafeCast.fromIntToShort(STOP.getId()));
			stopHeader.setDataSize(SafeCast.fromIntToShort(0));
			stopHeader.setElementCount(SafeCast.fromIntToShort(1));
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

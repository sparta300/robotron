package com.lbg.persist.structure;

import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.STOP;

import java.nio.ByteBuffer;

import javax.inject.Inject;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.SafeCast;
import com.lbg.persist.Swizzler;
import com.lbg.persist.structure.raw.Header;
import com.lbg.utility.PropertyMap;

/**
 * a factory for creating data structures.
 * 
 * @author C006011
 */
public class StructureFactoryImpl implements StructureFactory
{
	private final Swizzler swizzler;
	private final StructureLibrary library;
	
	@Inject
	private StructureFactoryImpl(StructureLibrary library, Swizzler swizzler)
	{
		this.library = library;
		this.swizzler = swizzler;
	}
	
	@Override
	public Structure create(StructureType structureType, ByteBuffer bb)
	{
		final BasicStructureCommand command = library.getBasicCommand(structureType);
		
		if (command == null)
		{
			throw new IllegalArgumentException("unknown structure type: " + structureType);
		}
			
		return command.create(bb, swizzler);
	}
	
	@Override
	public Header createStopHeader(ByteBuffer bb)
	{
		final Header stopHeader = (Header) create(HEADER, bb);
		stopHeader.setType(SafeCast.fromIntToShort(STOP.getId()));
		stopHeader.setDataSize(SafeCast.fromIntToShort(0));
		stopHeader.setElementCount(SafeCast.fromIntToShort(1));
		return stopHeader;
	}
	
	@Override
	public Structure createWithHeader(StructureType structureType, ByteBuffer bb, PropertyMap parameters) throws PersistenceException
	{
		final HeaderStructureCommand command = library.getHeaderCommand(structureType);
		
		if (command == null)
		{
			throw new IllegalArgumentException("unknown structure type: " + structureType);
		}

		return command.createWithHeader(bb, parameters);
	}
}

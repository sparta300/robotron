package com.lbg.persist.structure;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.Swizzler;
import com.lbg.persist.structure.raw.Header;

/**
 * knows how to read a known structure type from a byte buffer.
 * 
 * @author C006011
 */
public class StructureReaderImpl implements StructureReader
{
	private final StructureLibrary library;
	private final Swizzler swizzler;

	@Inject
	private StructureReaderImpl(StructureLibrary library, Swizzler swizzler)
	{
		this.library = library;
		this.swizzler = swizzler;
	}
	
	@Override
	public Structure readStructure(StructureType structureType, ByteBuffer bb)
	{
		final BasicStructureCommand command = library.getBasicCommand(structureType);
		
		if (command == null)
		{
			throw new IllegalArgumentException("unknown basic structure type: " + structureType);
		}
			
		return command.read(bb, swizzler);
	}

	@Override
	public Structure readStructure(StructureType structureType, Header header, ByteBuffer bb) throws PersistenceException
	{
		final HeaderStructureCommand command = library.getHeaderCommand(structureType);
		
		if (command == null)
		{
			throw new IllegalArgumentException("unknown complex structure type: " + structureType);
		}
		
		return command.read(bb, header, swizzler);
	}

	@Override
	public List<Structure> readComposite(List<StructureType> types, Header header, ByteBuffer bb) throws PersistenceException
	{
		final List<Structure> list = new ArrayList<Structure>(types.size());
		
		for (StructureType type : types)
		{
			final HeaderStructureCommand command = library.getHeaderCommand(type);
			
			if (command == null)
			{
				throw new IllegalArgumentException("type not available in library: " + type.name());
			}
			
			list.add(command.read(bb, header, swizzler)); 			
		}
		
		return list;
	}	
	
}

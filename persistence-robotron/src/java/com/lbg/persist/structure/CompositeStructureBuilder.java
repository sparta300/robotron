package com.lbg.persist.structure;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.lbg.persist.structure.raw.Header;

/**
 * a builder for composite structures.
 * 
 * @author C006011
 */
public class CompositeStructureBuilder
{
	private final Map<Integer, Structure> structureMap = new HashMap<Integer, Structure>();
	private final Map<Integer, StructureType> structureTypeMap = new HashMap<Integer, StructureType>();
	private final AtomicInteger index = new AtomicInteger();
	private final StructureType type;
	private final ByteBuffer bb;
	private final Header header;
	private final int startPosition;
 	
	public CompositeStructureBuilder(ByteBuffer bb, StructureType type, int startPosition, Header header)
	{
		this.bb = bb;		
		this.type = type;
		this.startPosition = startPosition;
		this.header = header;
	}
	
	public Structure build()
	{
		final int componentCount = structureMap.size();
		final List<Structure> components = new ArrayList<Structure>();
		final List<StructureType> componentTypes = new ArrayList<StructureType>();
		
		for (int c = 0; c < componentCount; c++)
		{
			components.add(structureMap.get(c));
			componentTypes.add(structureTypeMap.get(c));
		}
		
		// save our current position
		final int savedPosition = bb.position();
		
		// one of the rules of reading structures is that we need to start at start position when reading and must
		// set the position to the end of the structure whether you have read the entire thing or not
		bb.position(startPosition);
		final CompositeStructure structure = new CompositeStructure(bb, header, components, componentTypes);
		
		// go back to current position
		bb.position(savedPosition);
		 
		return structure;
	}

	
	public StructureType getStructureType()
	{
		return type;
	}
	
	public void addComponent(StructureType type, Structure structure)
	{
		structureMap.put(index.get(), structure);
		structureTypeMap.put(index.get(), type);
		index.incrementAndGet();
	}
	
	public Structure getComponent(int index)
	{
		return structureMap.get(index);
	}
		
	public StructureType getComponentType(int index)
	{
		return structureTypeMap.get(index);
	}
}

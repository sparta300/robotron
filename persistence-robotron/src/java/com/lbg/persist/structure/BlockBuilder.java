package com.lbg.persist.structure;

import java.util.ArrayList;
import java.util.List;

/**
 * builds up a picture of the structure of a block, allowing you to reference particular components using an index.
 * 
 * @author C006011
 */
public class BlockBuilder
{

	private final List<Structure> structureList = new ArrayList<Structure>();
	private final List<StructureType> structureTypeList = new ArrayList<StructureType>();
	
	public BlockBuilder()
	{
		super();
	}

	public void addComponent(StructureType type, Structure structure)
	{
		structureList.add(structure);
		structureTypeList.add(type);
	}
	
	public void addCompositeComponent(CompositeStructureBuilder compositeBuilder)
	{
		addComponent(compositeBuilder.getStructureType(), compositeBuilder.build());		
	}
		
	public Block build()
	{
		return new BlockImpl(structureList, structureTypeList);
	}
}

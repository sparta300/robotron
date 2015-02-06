package com.lbg.persist.structure;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

import com.lbg.persist.structure.header.AbstractStructureWithHeader;
import com.lbg.persist.structure.raw.Header;

/**
 * a structure that is itself made up of a number of smaller structures.
 * 
 * @author C006011
 */
public class CompositeStructure extends AbstractStructureWithHeader
{
	private final Header header;
	private final List<Structure> components;
	private final List<StructureType> componentTypes;
	
	public CompositeStructure(ByteBuffer bb, Header header, 
			                  List<Structure> components, List<StructureType> componentTypes)
	{
		super(bb, header);
		this.header = header;
		this.components = components;
		this.componentTypes = componentTypes;
	}

	@Override
	public void reset()
	{
		for (Structure component : components)
		{
			component.reset();
		}
	}
	
	public int getDataSize()
	{
		return header.getDataSize();
	}
	
	public StructureType getComponentType(int index)
	{
		return componentTypes.get(index);
	}	
	
	public Structure getComponent(int index)
	{
		return components.get(index);
	}	
	
	public Iterable<Structure> getComponents()
	{
		return Collections.unmodifiableList(components);
	}
}

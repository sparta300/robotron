package com.lbg.persist.structure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.lbg.persist.api.Resource;
import com.lbg.persist.api.Sink;
import com.lbg.persist.api.Source;
import com.lbg.persist.decorator.SinkListDecorator16;

/**
 * a block is a memory resident look-up table for the data in a disc block.
 * 
 * @author C006011
 */
public class BlockImpl implements Block
{
	private final Map<Integer, Structure> structureMap = new HashMap<Integer, Structure>();
	private final Map<Integer, StructureType> structureTypeMap = new HashMap<Integer, StructureType>();
	private final AtomicInteger index = new AtomicInteger();
	
	public BlockImpl(List<Structure> structureList,	List<StructureType> structureTypeList)
	{
		final int structureCount = structureList.size();
		assert structureCount == structureTypeList.size();
		
		for (Structure structure : structureList)
		{
			final Integer i = index.get();
			structureMap.put(i, structure);
			structureTypeMap.put(i, structureTypeList.get(i));
			index.incrementAndGet();
		}
	}
 	
	@Override
	public Sink<Double> asDoubleSink(int index)
	{
		final StructureType type = getComponentType(index);
		final Structure component = getComponent(index);
		
		return new SinkListDecorator16(type, component);
	}
	
	public Source<Double> asDoubleSource(int index)
	{
		final StructureType type = getComponentType(index);
		final Structure component = getComponent(index);
		
		return new SinkListDecorator16(type, component);
	}	
		
	public <T> Sink<T> asSink(int index)
	{
		return null;
	}
	
	public <T> Source<T> asSource(int index)
	{
		return null;
	}
	
	public <T> Resource<T> asResource(int index)
	{
		return null;
	}
	
	@Override
	public Structure getComponent(int index)
	{
		return structureMap.get(index);
	}
		
	@Override
	public StructureType getComponentType(int index)
	{
		return structureTypeMap.get(index);
	}
}

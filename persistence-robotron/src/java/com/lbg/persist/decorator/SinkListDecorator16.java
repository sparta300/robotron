package com.lbg.persist.decorator;

import java.util.ArrayList;
import java.util.List;

import com.lbg.persist.api.Sink;
import com.lbg.persist.api.Source;
import com.lbg.persist.mask.Mask7;
import com.lbg.persist.pointer.BytePointer;
import com.lbg.persist.structure.CompositeStructure;
import com.lbg.persist.structure.IndexableStructure;
import com.lbg.persist.structure.Structure;
import com.lbg.persist.structure.StructureType;
import com.lbg.persist.structure.header.BooleanList8;
import com.lbg.persist.structure.header.Count16;
import com.lbg.persist.structure.header.DoubleList;
import com.lbg.resource.OutOfSpace;
import com.lbg.resource.PhysicalResourceException;
import com.lbg.resource.ResourceEmpty;

public class SinkListDecorator16 implements Sink<Double>, Source<Double>
{
	private final IndexableStructure<Boolean, Double> indexableStructure;
	private final List<Integer> freeIndices = new ArrayList<Integer>();
	private final List<Integer> usedIndices = new ArrayList<Integer>();
 
	public SinkListDecorator16(StructureType type, Structure component)
	{
		if (type == StructureType.LIST_16 && component instanceof CompositeStructure)
		{
			final CompositeStructure composite = (CompositeStructure) component;
			// list-16
			// [0] count
			// [1] boolean-list
			// [2] double-list
			
			final int listSize = ((Count16) composite.getComponent(0)).getCount();
			final int maxIndex = listSize - 1;
			final BooleanList8 booleanList = (BooleanList8) composite.getComponent(1);
			final DoubleList doubleList = (DoubleList) composite.getComponent(2);
			
			indexableStructure = new IndexableStructure<Boolean, Double>() 
				{
					@Override
					public Boolean getManagement(int index)
					{
						assert index > 0 && index < maxIndex;
						final BytePointer pointer = booleanList.getPointer(index); 
						final Mask7 mask = new Mask7(pointer);
						return mask.isAllocated();
					}

					@Override
					public Double getElement(int index)
					{
						assert index > 0 && index < maxIndex;						
						return doubleList.get(index);
					}

					@Override
					public void setManagement(int index, Boolean value)
					{
						assert index > 0 && index < maxIndex;
						final BytePointer pointer = booleanList.getPointer(index); 
						final Mask7 mask = new Mask7(pointer);
						mask.setAllocated(value);
					}

					@Override
					public void setElement(int index, Double value)
					{
						assert index > 0 && index < maxIndex;
						doubleList.set(index, value);
					}
				};
				
				for (int i = 0; i < listSize; i++)
				{
					if (!indexableStructure.getManagement(i))
					{
						freeIndices.add(i);
					}
					else
					{
						usedIndices.add(i);
					}
				}
		}
		else
		{
			throw new IllegalArgumentException("structure type incompatible with a list: " + type.name());
		}
	}

	@Override
	public void put(Double data) throws PhysicalResourceException
	{
		if (freeIndices.size() == 0)
		{
			throw new OutOfSpace("list is full");
		}
	
		final int nextIndex = freeIndices.remove(0);
		indexableStructure.setManagement(nextIndex, true);
		indexableStructure.setElement(nextIndex, data);
	}

	@Override
	public Double get() throws PhysicalResourceException	
	{
		if (usedIndices.size() == 0)
		{
			throw new ResourceEmpty("list exhausted");
		}
		
		final int nextIndex = usedIndices.remove(0);
		return indexableStructure.getElement(nextIndex);
	}


}

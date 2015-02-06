package com.lbg.persist.structure;

import com.lbg.persist.api.Sink;
import com.lbg.persist.api.Source;

public interface Block
{

	Sink<Double> asDoubleSink(int index);
	
	Source<Double> asDoubleSource(int index);

	Structure getComponent(int index);

	StructureType getComponentType(int index);

}

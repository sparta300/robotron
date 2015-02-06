package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.Swizzler;
import com.lbg.utility.PropertyMap;

/**
 * command for creating a composite structure.
 * We do not need a read method because the parts of the composite will be read individually.
 * 
 * @author C006011
 */
public interface CompositeStructureCommand
{
	Structure create(ByteBuffer bb, Swizzler swizzler, PropertyMap parameters);
}

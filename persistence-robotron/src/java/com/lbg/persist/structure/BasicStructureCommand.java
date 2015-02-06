package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.Swizzler;

public interface BasicStructureCommand
{
	Structure read(ByteBuffer bb, Swizzler swizzler);
	Structure create(ByteBuffer bb, Swizzler swizzler);
}

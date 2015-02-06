package com.lbg.persist.structure;

import java.nio.ByteBuffer;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.Swizzler;
import com.lbg.persist.structure.raw.Header;
import com.lbg.utility.PropertyMap;

public interface HeaderStructureCommand
{
	Structure createWithHeader(ByteBuffer bb, PropertyMap parameters) throws PersistenceException;
	Structure read(ByteBuffer bb, Header header, Swizzler swizzler) throws PersistenceException;
}

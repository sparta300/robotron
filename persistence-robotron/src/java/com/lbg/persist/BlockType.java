package com.lbg.persist;

import java.util.HashMap;
import java.util.Map;

/**
 * identifies the full set of supported block types.
 * 
 * @author C006011
 */
public enum BlockType
{
	ROOT(0xbe),
	NORMAL(0x01),
	;
	
	private static final Map<Integer, BlockType> map = new HashMap<Integer, BlockType>();
	private final int typeId;
	
	static
	{
		for (BlockType type : values())
		{
			map.put(type.getTypeId(), type);
		}
	}
	
	private BlockType(int typeId)
	{
		this.typeId = typeId;
	}
	
	public int getTypeId()
	{
		return typeId;
	}

	public static BlockType forTypeId(int blockType)
	{
		final BlockType value = map.get(blockType);

		if (value == null)
		{
			throw new IllegalArgumentException("blockType " + blockType	+ " not recognised");
		}

		return value;
	}
}

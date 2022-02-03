package org.hydroid.beowulf;

import java.util.HashMap;
import java.util.Map;

/**
 * @author smiley
 */
public enum BlockType
{
	ROOT_BLOCK((int) 0xbe, "root"), 
	STORAGE_BLOCK(1, "store"),
	WILDERNESS_BLOCK(2, "wild"),
	;

	private final int blockType;
	private final String description;

	private static final Map<Integer, BlockType> map = new HashMap<Integer, BlockType>();

	static
	{
		map.put(ROOT_BLOCK.blockType, ROOT_BLOCK);
		map.put(STORAGE_BLOCK.blockType, STORAGE_BLOCK);
		map.put(WILDERNESS_BLOCK.blockType, WILDERNESS_BLOCK);
	}

	private BlockType(int blockType, String description)
	{
		this.blockType = blockType;
		this.description = description;
	}

	@Override
	public String toString()
	{
		return this.description;
	}

	public static String toString(int blockType)
	{
		return fromInt(blockType).toString();
	}

	public int blockType() { return this.blockType; }
	public String description() { return this.description; }

	public static BlockType fromInt(int blockType)
	{
		final BlockType value = map.get(blockType);

		if (value == null)
		{
			throw new IllegalArgumentException("blockType " + blockType	+ " not allowed");
		}

		return value;
	}
}

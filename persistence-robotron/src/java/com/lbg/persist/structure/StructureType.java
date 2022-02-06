package com.lbg.persist.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * an enumeration of the structures that are supported.
 * 
 * @author C006011
 */
public enum StructureType
{
	// basic
	MAGIC                     (1),
	GEOMETRY                  (2),
	VERSION                   (3),
	WILDERNESS                (4),
	HEADER                    (5),
	BLOCK_MAIN                (6),
	STORE_MAIN                (7),
	
	// structures with headers
	LIST_16                   (50),
	COUNT_16                  (51),
	LIST_BOOLEAN_8            (52),
	LIST_DOUBLE               (53),
	LIST_INTEGER              (54),
	STRING_DATA               (55),
	
	// telemetry
	TELEMETRY_STREAM          (90),
	TELEMETRY_FRAME           (91),
	FRAME_MAIN                (92),	
	LIST_TELEMETRY_FRAME      (93),	
	TIME_CODE                 (94),
	FRAME_SEGMENT             (95),
	
	// relationships
	RELATIONSHIP              (100),
	RELATIONSHIP_SET          (101),
	
	// composite structures
	START_COMPOSITE           (126),
	FINISH_COMPOSITE          (127),
	
	STOP                      (128),
	;

	private final int id;
	private static final Map<Integer, StructureType> idMap = new HashMap<Integer, StructureType>();

	static
	{
		for (StructureType type : values())
		{
			idMap.put(type.getId(), type);
		}
	}

	private StructureType(int id)
	{
		this.id = id;
	}
	
	public static StructureType forId(int id)
	{
		return idMap.get(id);
	}	

	public int getId()
	{
		return id;
	}

	@Override	
	public String toString()
	{
		return "[" + id + "] " + name();
	}
}

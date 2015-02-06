package com.lbg.persist;

import java.util.HashMap;
import java.util.Map;

public enum RelationshipType
{
	PARENT(1),
	CHILD(2),
	PREVIOUS(3),
	NEXT(4),
	;

	private static final Map<Integer, RelationshipType> idMap = new HashMap<Integer, RelationshipType>();
	
	static
	{
		for (RelationshipType type : values())
		{
			idMap.put(type.getTypeId(), type);
		}
	}
	
	private final int typeId;
	
	private RelationshipType(int typeId)
	{
		this.typeId = typeId;
	}
	
	public int getTypeId()
	{
		return typeId;
	}
	
	public static RelationshipType forTypeId(int id)
	{
		return idMap.get(id);
	}
}

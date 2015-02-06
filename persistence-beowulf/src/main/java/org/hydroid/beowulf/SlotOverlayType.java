/**
 * File		: $Source:$
 * Author	: $Author:$
 * Revision	: $Revision:$
 * Date 	: $Date:$
 */
package org.hydroid.beowulf;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_SLOT_OVERLAY_TYPE;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author smiley
 */
public enum SlotOverlayType
{
	
	UNSET(UNSET_SLOT_OVERLAY_TYPE, "UNSET"), 
	LL_LIFO_HEAD(1, "ll-lifo"), 
	LL_LIFO_SEGMENT(2, "ll-lifo-seg"), 
	INVALID(-1, "X");

	private final int typeId;
	private final String description;

	private static final Logger log = LoggerFactory.getLogger(SlotOverlayType.class);

	private static final Map<Integer, SlotOverlayType> map = new HashMap<Integer, SlotOverlayType>();

	static
	{
		map.put(UNSET.getTypeId(), UNSET);
		map.put(LL_LIFO_HEAD.getTypeId(), LL_LIFO_HEAD);
		map.put(LL_LIFO_SEGMENT.getTypeId(), LL_LIFO_SEGMENT);
		map.put(INVALID.getTypeId(), INVALID);
	}

	public static final int LL_LIFO_HEAD_TYPE = 1;
	public static final int LL_LIFO_SEGMENT_TYPE = 2;
	
	private SlotOverlayType(int typeId, String description)
	{
		this.typeId = typeId;
		this.description = description;
	}

	public String toString()
	{
		return description;
	}

	public static String toString(int slotOverlayType)
	{
		return fromInt(slotOverlayType).toString();
	}

	public int getTypeId()
	{
		return typeId;
	}

	public static SlotOverlayType fromInt(int typeId)
	{
		SlotOverlayType value = map.get(typeId);

		if (value == null)
		{
			log.debug("slot overlay type ID " + typeId + " not allowed");
			return INVALID;
		}

		return value;
	}
}

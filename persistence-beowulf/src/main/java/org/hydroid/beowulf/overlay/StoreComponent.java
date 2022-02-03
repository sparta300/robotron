package org.hydroid.beowulf.overlay;

import java.util.HashMap;
import java.util.Map;

public enum StoreComponent {
	METADATA(5000, "md"),
	SIZING(5001, "sz"),
	BLOCK_OVERHEAD(5002, "bo"),
	REPOSITORY_OVERHEAD(5003, "ro"),
	SLOT_OVERHEAD(5004, "so"),
	SANDPIT(5005, "sp"),
	FREE_SLOT_LIST(5006, "fsl"),
	FREE_LIST_RUNTIME(5007, "flr"),
	FREE_LIST_256(5008, "fl-256"),
	SINGLY_LINKED_LIST_HEAD(5009, "ll-htl"),
	SINGLY_LINKED_LIST_SEGMENT(5010, "ll-seg1"),
	;
	
	private final int id;
	private final String key;
	private final String stringified;
	private static final Map<Integer, StoreComponent> byId = new HashMap<Integer, StoreComponent>();
	private static final Map<String, StoreComponent> byKey = new HashMap<String, StoreComponent>();
	
	static {
		for (StoreComponent component : values()) {
			byId.put(component.id, component);
			byKey.put(component.key, component);
		}
	}
	
	private StoreComponent(int id, String key) {
		this.id = id;
		this.key = key;
		this.stringified = "id=" + id + " key=" + key; 
	}
	
	public static StoreComponent forId(int id) {
		return byId.get(id);
	}
	
	public static StoreComponent forKey(String key) {
		return byKey.get(key);
	}
	
	@Override
	public String toString() { return stringified; }
	
	public String key() { return key; }
	public int id() { return id; }
}

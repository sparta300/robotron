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
	private static final Map<Integer, StoreComponent> lookUp = new HashMap<Integer, StoreComponent>();
	
	static {
		lookUp.put(METADATA.id, METADATA);
		lookUp.put(SIZING.id, SIZING);
		lookUp.put(BLOCK_OVERHEAD.id, BLOCK_OVERHEAD);
		lookUp.put(REPOSITORY_OVERHEAD.id, REPOSITORY_OVERHEAD);
		lookUp.put(SLOT_OVERHEAD.id, SLOT_OVERHEAD);
		lookUp.put(SANDPIT.id, SANDPIT);
		lookUp.put(FREE_SLOT_LIST.id, FREE_SLOT_LIST);
		lookUp.put(FREE_LIST_RUNTIME.id, FREE_LIST_RUNTIME);
		lookUp.put(FREE_LIST_256.id, FREE_LIST_256);
		lookUp.put(SINGLY_LINKED_LIST_HEAD.id, SINGLY_LINKED_LIST_HEAD);
		lookUp.put(SINGLY_LINKED_LIST_SEGMENT.id, SINGLY_LINKED_LIST_SEGMENT);
	}
	
	private StoreComponent(int id, String key) {
		this.id = id;
		this.key = key;
		this.stringified = "id=" + id + " key=" + key; 
	}
	
	public static StoreComponent forId(int id) {
		return lookUp.get(id);
	}
	
	@Override
	public String toString() { return stringified; }
	
	public String key() { return key; }
	public int id() { return id; }
}

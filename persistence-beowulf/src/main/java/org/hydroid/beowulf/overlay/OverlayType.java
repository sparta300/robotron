package org.hydroid.beowulf.overlay;

import java.util.HashMap;
import java.util.Map;

public enum OverlayType {
	MAGIC("ma"), 
	SIZING("sz"), 
	REPOSITORY_OVERHEAD("ro"), 
	BLOCK_HEADER("bh"), 
	GENERAL_STORAGE("st"), 
	SANDPIT("sp");
	
	private OverlayType(String code) {
		this.code = code;
	}
	
	public static OverlayType forCode(String code) {
		return map.get(code);
	}
	
	public String getCode() {
		return code;
	}
	
	private static final Map<String, OverlayType> map = new HashMap<String, OverlayType>();
	
	static {
		map.put("ma", MAGIC);
		map.put("sz", SIZING);
		map.put("ro", REPOSITORY_OVERHEAD);
		map.put("bh", BLOCK_HEADER);
		map.put("st", GENERAL_STORAGE);
		map.put("sp", SANDPIT);
	}
	
	private final String code;
}

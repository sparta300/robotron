package com.lbg.persist.engine;

public enum RegisteredNames
{
	BLOCK_MAIN("block-main"),
	STORE_MAIN("store-main"),
	GEOMETRY("geometry"),
	MANIFEST("manifest"),
	VERSION_NUMBER("version-number"),
	;
	
	private final String key;
	
	private RegisteredNames(String key)
	{
		this.key = key;
	}
	
	public String getKey()
	{
		return key;
	}
	
	@Override	
	public String toString()
	{
		return key;
	}
}

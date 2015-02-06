package com.lbg.utility;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface PropertyMap 
{
	String getString(String key) throws MissingProperty;	
	List<String> getStringList(String key) throws MissingProperty;
	Integer getInteger(String key) throws MissingProperty;
	Long getLong(String string) throws MissingProperty;	
	Boolean getBoolean(String key) throws MissingProperty;
	Map<String, String> getMap();
	Properties getProperties();
}

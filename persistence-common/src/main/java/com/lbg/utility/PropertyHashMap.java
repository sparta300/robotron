package com.lbg.utility;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hydroid.file.FileHelper;

/**
 * make a properties available as a map and a properties object.
 * 
 * @author C006011
 */
public class PropertyHashMap implements PropertyMap
{
	private final Map<String, String> map;
	private final Properties properties = new Properties(); 

	public PropertyHashMap(String fileName) throws IOException
	{
		map = FileHelper.loadPropertyMap(fileName);
		properties.putAll(map);
	}
	
	public PropertyHashMap(Map<String, String> map)
	{
		this.map = map;
		properties.putAll(map);		
	}
	
	@Override	
	public String getString(String key) throws MissingProperty
	{
		final String value = map.get(key);
		
		if (value == null)
		{
			throw new MissingProperty(key);
		}
		
		return value;
	}
	
	@Override	
	public List<String> getStringList(String key) throws MissingProperty
	{
		final String list = getString(key);
		
		if (list == null)
		{
			throw new MissingProperty(key);
		}
		
		return Arrays.asList(list.split(","));		
	}

	@Override	
	public Integer getInteger(String key) throws MissingProperty
	{
		final String value = getString(key);
		
		if (value == null)
		{
			throw new MissingProperty(key);
		}
		
		return Integer.parseInt(value);
	}
	
	@Override	
	public Long getLong(String key) throws MissingProperty
	{
		final String value = getString(key);
		
		if (value == null)
		{
			throw new MissingProperty(key);
		}
		
		return Long.parseLong(value);
	}	
	
	@Override	
	public Boolean getBoolean(String key) throws MissingProperty
	{
		final String value = getString(key);
		
		if (value == null)
		{
			throw new MissingProperty(key);
		}
		
		return value.equals("true") ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public Map<String, String> getMap() 
	{
		return Collections.unmodifiableMap(map);
	}

	@Override
	public Properties getProperties()
	{
		return properties;
	}
}

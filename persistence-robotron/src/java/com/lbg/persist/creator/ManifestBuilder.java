package com.lbg.persist.creator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.lbg.utility.json.JsonArray;
import com.lbg.utility.json.JsonObject;
import com.lbg.utility.json.JsonValue;

/**
 * a builder for the repository manifest.
 * This is a wafer-thin wrapper around the minimal json stuff.
 * 
 * @author C006011
 */
public class ManifestBuilder
{
	private final JsonObject root;
	private final List<String> services;
	private final List<JsonObject> structures;
	private final Set<String> structureNames;
	
	public ManifestBuilder()
	{
		root = new JsonObject();
		services = new ArrayList<String>();
		structures = new ArrayList<JsonObject>();
		structureNames = new HashSet<String>();
	}
	
	public ManifestBuilder withAttribute(String name, JsonValue value)
	{
		root.set(name, value);
		return this;
	}
	
	public ManifestBuilder withAttribute(String name, String value)
	{
		root.set(name, JsonValue.valueOf(value));
		return this;
	}
	
	public ManifestBuilder withAttribute(String name, int value)
	{
		root.set(name, JsonValue.valueOf(value));
		return this;
	}	
	
	public ManifestBuilder withAttribute(String name, long value)
	{
		root.set(name, JsonValue.valueOf(value));
		return this;
	}	
	
	public ManifestBuilder withContentType(String contentType)
	{
		root.set("content-type", contentType);
		return this;
	}
	
	public ManifestBuilder addService(String serviceName)
	{
		if (!services.contains(serviceName))
		{
			services.add(serviceName);
		}
		
		return this;
	}
	
	public ManifestBuilder addStructure(String structureName, long address)
	{
		if (!structureNames.contains(structureName))
		{
			structureNames.add(structureName);
			final JsonObject structure = new JsonObject();
			structure.add("name", structureName);
			structure.add("address", address);
			structures.add(structure);
		}
		
		return this;
	}	
	
	public String build()
	{
		if (!structureNames.isEmpty())
		{
			final JsonArray list = new JsonArray();
			
			for (JsonObject structure : structures)
			{
				list.add(structure);
			}
			
			root.set("structures", list);
		}
		
		if (!services.isEmpty())
		{
			final JsonArray list = new JsonArray();
			
			for (String serviceName : services)
			{
				list.add(serviceName);
			}
			
			root.set("services", list);
		}
		
		return root.toString();
	}
}

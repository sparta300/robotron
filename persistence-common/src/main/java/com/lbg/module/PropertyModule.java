package com.lbg.module;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.hydroid.file.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.lbg.utility.PropertyHashMap;
import com.lbg.utility.PropertyMap;

/**
 * a module which attempts to smooth out any differences between different runtime environments.
 * 
 * @author C006011
 */
public class PropertyModule extends AbstractModule
{
	private static final Logger log = LoggerFactory.getLogger(PropertyModule.class);
	
	private final String propertyFileName;
	
	public PropertyModule(String propertyFileName)
	{
		this.propertyFileName = propertyFileName;
	}
	
	@Override
	protected void configure() 
	{
		// bind in the environment we are using
		final String environment = System.getProperty("environment", "prod");
		bind(String.class).annotatedWith(Names.named("environment")).toInstance(environment);
		
		// load the property file for the given environment.]
		final String propertyFileForEnv = FileHelper.deriveFileNameForEnv(propertyFileName, environment);
		bindProperties(propertyFileForEnv);
	}
	
	private void bindProperties(String fileName)
	{
		PropertyMap rawProperties;
		
		try 
		{
			rawProperties = new PropertyHashMap(fileName);
		}
		catch (IOException e) 
		{			
			throw new RuntimeException("property module load failed: cannot load property file " + fileName);
		}
		
		final Map<String, String> overriddenProperties = overrideWithSystemProperties(rawProperties);
		
		// bind the names so they can be injected individually, for example as:
		// @Inject private MyClass(@Named("java.version") String javaVersion)
		Names.bindProperties(binder(), overriddenProperties);
		
		// make property map itself available for injection too, for example as:
		// @Inject private MyClass(PropertyMap props)
		final PropertyHashMap map = new PropertyHashMap(overriddenProperties);
		bind(PropertyMap.class).toInstance(map);
		
		// make the Properties object available too, for example as:
		// @Inject private MyClass(Properties props)
		bind(Properties.class).toInstance(map.getProperties());
	
	}
	
	/**
	 * override the given properties system properties.
	 * The main reason for this is to allow you to override normal key/value pairs from the properties file
	 * via the command line using VM arguments of the form <tt>-Dname=value</tt>.
	 * The reason this looks complicated is because the following code snippet causes the binder to blow up
	 * (presumably because each name can only be bound once):
	 * <pre>
	 * Names.bindProperties(binder(), propertyMap.getMap());
	 * Names.bindProperties(binder(), System.getProperties());
	 * </pre>
	 * Instead, we create a mutable copy of the loaded properties and add the system properties on top
	 * of them.
	 *  
	 * @param propertyMap the properties loaded from the properties file
	 * @return the new mutable map containing the combined properties
	 */
	private Map<String, String> overrideWithSystemProperties(PropertyMap propertyMap)
	{
		// bind the system properties, 
		final Properties systemProperties = System.getProperties();
		Names.bindProperties(binder(), systemProperties);
		
		// create a mutable copy of the property map
		final Map<String, String> props =  new HashMap<String, String>(propertyMap.getMap());

		// spin through the property map and override with system properties
		for (String key : systemProperties.stringPropertyNames())
		{
			final String value = systemProperties.getProperty(key);
						
			if (props.containsKey(key))
			{
				log.info("system override: " + key + "=" + value);
			}
			
			props.put(key, value);
		}

		return props;
	}
}

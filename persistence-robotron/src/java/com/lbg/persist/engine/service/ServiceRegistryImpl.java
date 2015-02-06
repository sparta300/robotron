package com.lbg.persist.engine.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceRegistryImpl implements ServiceRegistry
{
	private final Map<String, Service> serviceMap = new HashMap<String, Service>();

	@Override
	public void register(String serviceName, Service service)
	{
		serviceMap.put(serviceName, service);		
	}

	@Override
	public void deregister(String serviceName)
	{
		serviceMap.remove(serviceName);
	}

	@Override
	public Service lookUp(String name)
	{		
		return serviceMap.get(name);
	}
}

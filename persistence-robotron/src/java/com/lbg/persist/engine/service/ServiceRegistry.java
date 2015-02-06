package com.lbg.persist.engine.service;


public interface ServiceRegistry
{
	void register(String name, Service service);
	
	void deregister(String name);
	
	Service lookUp(String name);
}

package com.lbg.persist.engine.service;

import com.lbg.utility.PropertyMap;

/**
 * a very basic interface for controlling repository services.
 * 
 * @author C006011
 */
public interface Service
{
	void up(PropertyMap parameters) throws ServiceException;
	void down();
}

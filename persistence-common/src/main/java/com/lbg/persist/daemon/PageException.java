package com.lbg.persist.daemon;

import com.lbg.resource.PhysicalResourceException;

public class PageException extends PhysicalResourceException
{
	/**
	 * default serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	public PageException(String message)
	{
		super(message);
	}

	public PageException(String message, Throwable cause)
	{
		super(message, cause);
	}
}

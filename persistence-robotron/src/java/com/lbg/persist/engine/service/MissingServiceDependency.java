package com.lbg.persist.engine.service;

/**
 * thrown when a service detects that one of its mandatory dependent services is not available.
 * 
 * @author C006011
 */
public class MissingServiceDependency extends ServiceException
{
	private static final long serialVersionUID = 1L;

	public MissingServiceDependency(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MissingServiceDependency(String message)
	{
		super(message);
	}
}

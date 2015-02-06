package com.lbg.resource;


public class ResourceRemoved extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public ResourceRemoved(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceRemoved(String message)
	{
		super(message);
	}

	public ResourceRemoved()
	{
		super();
	}

}

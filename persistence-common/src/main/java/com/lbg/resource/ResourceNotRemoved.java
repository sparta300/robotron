package com.lbg.resource;


public class ResourceNotRemoved extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public ResourceNotRemoved(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceNotRemoved(String message)
	{
		super(message);
	}

	public ResourceNotRemoved()
	{
		super();
	}

}

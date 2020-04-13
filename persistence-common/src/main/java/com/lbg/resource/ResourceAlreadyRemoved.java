package com.lbg.resource;

import org.hydroid.file.PhysicalResourceException;

public class ResourceAlreadyRemoved extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyRemoved(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceAlreadyRemoved(String message)
	{
		super(message);
	}

	public ResourceAlreadyRemoved()
	{
		super();
	}

}

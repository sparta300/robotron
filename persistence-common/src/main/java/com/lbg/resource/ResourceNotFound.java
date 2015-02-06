package com.lbg.resource;


public class ResourceNotFound extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public ResourceNotFound(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceNotFound(String message)
	{
		super(message);
	}

	public ResourceNotFound()
	{
		super();
	}

}

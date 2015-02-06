package com.lbg.resource;


public class ResourceAlreadyOpen extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;
	
	public ResourceAlreadyOpen(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceAlreadyOpen(String message)
	{
		super(message);
	}

}

package com.lbg.resource;


public class ResourceEmpty extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public ResourceEmpty(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceEmpty(String message)
	{
		super(message);
	}

	public ResourceEmpty()
	{
		super();
	}

}

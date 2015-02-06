package com.lbg.resource;

public class PhysicalResourceException extends Exception
{
    /**
	 * serial version UID
	 */
	private static final long serialVersionUID = 1L;

	public PhysicalResourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PhysicalResourceException(String message)
	{
		super(message);
	}
	
	public PhysicalResourceException()
	{
		super();
	}
}

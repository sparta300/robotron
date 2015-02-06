package com.lbg.utility;


public class MissingProperty extends RuntimeException
{
	/**
	 * serial version
	 */
	private static final long serialVersionUID = 1L;

	public MissingProperty(String propertyName)
	{
		super("missing property: " + propertyName);
	}
}

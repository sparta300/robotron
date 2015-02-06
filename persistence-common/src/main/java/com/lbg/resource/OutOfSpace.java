package com.lbg.resource;


public class OutOfSpace extends PhysicalResourceException
{
	private static final long serialVersionUID = 1L;

	public OutOfSpace(String message, Throwable cause)
	{
		super(message, cause);
	}

	public OutOfSpace(String message)
	{
		super(message);
	}

	public OutOfSpace()
	{
		super();
	}

	public OutOfSpace(int transactionId, int remainingSlotCount)
	{
		super("tx=" + transactionId + " remainingSlots=" + remainingSlotCount);
	}

}

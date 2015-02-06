package com.lbg.persist.main;

public class Calculator
{
	private final int fixedSize;
	private final int spacePerElement;

	public Calculator(int fixedSize, int spacePerElement)
	{
		this.fixedSize = fixedSize;
		this.spacePerElement = spacePerElement;
	}
	
	public int getElementCount(int maxSpace)
	{
		int space = maxSpace - fixedSize;
		return space / spacePerElement;
	}
}

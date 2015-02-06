package com.lbg.persist.structure;


public class ListHelperImpl implements ListHelper
{
	@Override
	public int calculateSize(int maxSpace, int preambleSize, int spacePerElement)
	{
		int space = maxSpace - preambleSize;
		return space / spacePerElement;
	}
	
	@Override
	public int calculateSize(int maxSpace, int preambleSize, int managementSize, int elementSize)
	{
		int space = maxSpace - preambleSize;
		int spacePerElement = managementSize + elementSize;
		return space / spacePerElement;
	}

}

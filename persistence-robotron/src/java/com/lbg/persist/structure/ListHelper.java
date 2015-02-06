package com.lbg.persist.structure;


public interface ListHelper
{

	int calculateSize(int maxSpace, int preambleSize, int spacePerElement);

	int calculateSize(int maxSpace, int preambleSize, int managementSize, int elementSize);

}

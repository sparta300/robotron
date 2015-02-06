package com.lbg.persist.daemon;

public interface AlgorithmCallback
{
	boolean isPinned(int index) throws PageException;
}

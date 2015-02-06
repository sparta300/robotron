package com.lbg.persist.daemon;

import static com.lbg.persist.daemon.PageConstants.NOT_SET;

public class PageEntry
{
	private long lastUse = NOT_SET;
	
	public PageEntry()
	{
		stamp();
	}

	public void stamp()
	{
		lastUse = System.currentTimeMillis();
	}

	public long getLastUseTimeStamp()
	{
		return lastUse;
	}
}

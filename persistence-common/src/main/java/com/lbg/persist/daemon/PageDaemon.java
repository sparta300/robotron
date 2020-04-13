package com.lbg.persist.daemon;

import org.hydroid.file.PhysicalResourceException;

public interface PageDaemon extends AlgorithmCallback
{

	Page pageIn(PageIdentifier pageId) throws PhysicalResourceException;

	void pin(Page page);

	void unpin(Page page);

	Page fetch(PageIdentifier pageId) throws PhysicalResourceException;

	void pageOut(Page page) throws PageException;

	void flushAll();

	Page make(PageIdentifier page) throws PhysicalResourceException;

	public int getPageLimit();
}

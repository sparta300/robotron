package com.lbg.persist.daemon;

import com.lbg.file.RepositoryFile;

/**
 * uniquely allows you to identify a block of raw data within a file.
 * 
 * @author C006011
 */
public class PageIdentifier
{
	private final RepositoryFile file;
	private final long position;
	private final int byteCount;
	
	public PageIdentifier(long blockId, int blockSize, RepositoryFile file)
	{
		this.file = file;
		this.position = blockId * blockSize;
		this.byteCount = blockSize;		
	}
	
	public PageIdentifier(RepositoryFile file, long position, int byteCount)
	{
		this.file = file;
		this.position = position;
		this.byteCount = byteCount;
	}

	public int getByteCount()
	{
		return byteCount;
	}

	public long getPosition()
	{
		return position;
	}

	public RepositoryFile getFile()
	{
		return file;
	}

	public String toString()
	{
		return String.format("pos=%d size=%d mode=%s", position, byteCount,	file.getFileMode().toString());
	}
}

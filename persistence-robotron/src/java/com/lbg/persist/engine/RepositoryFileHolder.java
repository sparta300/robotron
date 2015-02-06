package com.lbg.persist.engine;

import java.util.concurrent.atomic.AtomicReference;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.structure.raw.Geometry;

public class RepositoryFileHolder
{
	private final RepositoryFile file;	
	private final TranslationLookasideBuffer tlb;
	private final AtomicReference<Integer> blockSizeRef = new AtomicReference<Integer>();
	private final AtomicReference<Integer> maxBlockCountRef = new AtomicReference<Integer>();
	
	public RepositoryFileHolder(RepositoryFile file, TranslationLookasideBuffer tlb)
	{
		this.file = file;
		this.tlb = tlb;
	}
	
	public RepositoryFile getFile()
	{
		return file;
	}
	
	public void setGeometry(Geometry geometry)
	{
		final Integer blockSize = blockSizeRef.get();
		
		if (blockSize != null)
		{
			return; 
		}
		
		blockSizeRef.set(geometry.getBlockSize());
		maxBlockCountRef.set(geometry.getMaxBlockCount());
	}
	
	public int getBlockSize()
	{
		final Integer blockSize = blockSizeRef.get();
		
		if (blockSize == null)
		{
			throw new RuntimeException("no block size set");
		}
		
		return blockSize;
	}
	
	public int getMaxBlockCount()
	{
		final Integer maxBlockCount = maxBlockCountRef.get();
		
		if (maxBlockCount == null)
		{
			throw new RuntimeException("no block size set");
		}
		
		return maxBlockCount;
	}

	public TranslationLookasideBuffer getTlb()
	{
		return tlb;
	}	
}

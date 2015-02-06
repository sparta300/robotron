package com.lbg.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * a file name filter that looks for files with a specific suffix.
 * 
 * @author C006011
 */
public class FileSuffixFilter implements FilenameFilter
{
	private final List<String> list;

	public FileSuffixFilter(String suffix)
	{
		this(new String[] { suffix });
	}

	public FileSuffixFilter(List<String> suffixList)
	{
		list = new ArrayList<String>(suffixList.size());

		for (String suffix : suffixList)
		{
			addSuffix(suffix);
		}
	}

	public FileSuffixFilter(String... suffices)
	{
		int suffixCount = suffices.length;
		list = new ArrayList<String>(suffixCount);

		for (String suffix : suffices)
		{
			addSuffix(suffix);
		}
	}

	private void addSuffix(String suffix)
	{
		if (!suffix.startsWith("."))
		{
			suffix = "." + suffix;
		}

		list.add(suffix);
	}

	public boolean accept(File dir, String fileName)
	{
		for (String suffix : list)
		{
			if (fileName.endsWith(suffix))
			{
				return true;
			}
		}

		return false;
	}
}

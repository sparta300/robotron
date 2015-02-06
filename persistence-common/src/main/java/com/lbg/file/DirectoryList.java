package com.lbg.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DirectoryList implements SearchPath
{
	private static final Logger logger = LoggerFactory.getLogger(DirectoryList.class);
	
	private final List<File> directories;

	public DirectoryList(List<String> directories)
	{
		this.directories = new ArrayList<File>(directories.size());

		for (String dir : directories)
		{
			add(new File(dir.trim()));
		}
	}

	public void add(File directory)
	{
		directories.add(directory);
	}

	public File search(final String fileName) throws FileNotFoundException
	{
		FilenameFilter filter = new FilenameFilter()
		{

			@Override
			public boolean accept(File dir, String name)
			{
				if (name.equals(fileName))
				{
					logger.debug(String.format("found %s in directory %s", name, dir.getAbsolutePath()));
					return true;
				}

				return false;
			}
		};

		for (File dir : directories)
		{
			File[] files = dir.listFiles(filter);

			if (files.length > 0)
			{
				return files[0];
			}
		}

		throw new FileNotFoundException(fileName);
	}
}

package com.lbg.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a thin wrapper around {@link File File} which assumes that the underlying
 * file is a directory
 */
public class Directory
{

	private final File file;
	
	public Directory(File file)
	{
		this.file = file;
	}

	public Directory(String directoryName)
	{
		this(new File(directoryName));
	}

	public File getFile()
	{
		return file;
	}

	public String getFileName()
	{
		return file.getName();
	}

	public String getAbsolutePath()
	{
		return file.getAbsolutePath();
	}

	public Directory getParent()
	{
		return new Directory(file.getParentFile());
	}

	static public File getChildFile(String directoryName, String childName)
	{
		return new File(directoryName + File.separator + childName);
	}

	public File getChildFile(String childName)
	{
		return new File(file.getAbsolutePath() + File.separator + childName);
	}

	public Directory getChildDirectory(String childName)
	{
		return new Directory(new File(file.getAbsolutePath() + File.separator + childName));
	}

//	public List<Directory> listChildDirectories(String matchingPattern)
//	{
//		return listChildDirectories(new DirectoryNamePatternFilter(matchingPattern));
//	}

	public List<Directory> listChildDirectories(FilenameFilter filter)
	{
		final File[] children = getChildFiles(filter);
		final int count = children.length;

		if (count == 0)
		{
			return Collections.emptyList();
		}

		final List<Directory> list = new ArrayList<Directory>(count);

		for (File f : children)
		{
			if (f.isDirectory())
			{
				list.add(new Directory(f));
			}
		}

		return list;
	}

//	public List<File> listChildFiles(String matchingPattern)
//	{
//		return listChildFiles(new DirectoryNamePatternFilter(matchingPattern));
//	}

	public List<File> listChildFiles(FilenameFilter filter)
	{
		final File[] children = getChildFiles(filter);
		final int count = children.length;

		if (count == 0)
		{
			return Collections.emptyList();
		}

		final List<File> list = new ArrayList<File>(count);

		for (File f : children)
		{
			if (f.isFile())
			{
				list.add(f);
			}
		}

		return list;
	}

	public boolean exists()
	{
		return file.exists();
	}

	public boolean childExists(String childName)
	{
		final File child = new File(file.getAbsolutePath() + File.separator	+ childName);
		return child.exists();
	}

	public String[] getChildNames(FilenameFilter filter)
	{
		return getChildNames(file.list(filter));
	}

	public String[] getChildNames()
	{
		return getChildNames(file.list());
	}

	private String[] getChildNames(String[] shortFileNames)
	{
		final int fileCount = shortFileNames.length;
		final String[] fullFileNames = new String[fileCount];

		for (int f = 0; f < fileCount; f++)
		{
			fullFileNames[f] = file.getAbsolutePath() + File.separator + shortFileNames[f];
		}

		return fullFileNames;
	}

	public File[] getChildFiles()
	{
		final String[] fileNames = file.list();
		final int fileCount = fileNames.length;
		final File[] files = new File[fileCount];

		for (int f = 0; f < fileCount; f++)
		{
			files[f] = new File(file.getAbsolutePath() + File.separator	+ fileNames[f]);
		}

		return files;
	}

	public File[] getChildFiles(FilenameFilter filter)
	{
		final String[] fileNames = file.list(filter);

		if (fileNames == null)
		{
			return new File[] {};
		}

		final int fileCount = fileNames.length;
		final File[] files = new File[fileCount];

		for (int f = 0; f < fileCount; f++)
		{
			files[f] = new File(file.getAbsolutePath() + File.separator	+ fileNames[f]);
		}

		return files;
	}

	public List<File> getChildFileList(FilenameFilter filter)
	{
		final File[] files = getChildFiles(filter);
		final int fileCount = files.length;
		final List<File> list = Collections.synchronizedList(new ArrayList<File>(fileCount));

		for (int f = 0; f < fileCount; f++)
		{
			list.add(files[f]);
		}
		
		return list;
	}

	public List<Directory> listChildDirectories()
	{
		final String[] fileNames = file.list();
		final int fileCount = fileNames.length;
		final List<Directory> children = new ArrayList<Directory>(fileCount);

		for (String fileName : fileNames)
		{
			final File directory = new File(file.getAbsolutePath() + File.separator + fileName);

			if (directory.isDirectory())
			{
				children.add(new Directory(directory));
			}
		}

		return children;
	}

	public String toString()
	{
		return file.getAbsolutePath();
	}
}

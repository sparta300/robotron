package com.lbg.file;

import static com.lbg.utility.Characters.DOT_STRING;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * represents a single repository file.
 * 
 * @author C006011
 */
public class RepositoryFile
{
	private final FileMode fileMode;
	private final FileChannel channel;
	private final RandomAccessFile raf;
	private final String path;

	private static final Logger logger = LoggerFactory.getLogger(RepositoryFile.class);

	public RepositoryFile(SearchPath path, String repositoryName, String suffix, String fileMode) throws FileNotFoundException
	{
		this(path, repositoryName, suffix, FileMode.toFileMode(fileMode));
	}

	public RepositoryFile(String repositoryName, String suffix, String fileMode) throws FileNotFoundException
	{
		this(getFile(repositoryName, suffix, fileMode), FileMode.toFileMode(fileMode));
	}

	public RepositoryFile(File parentDirectory, String repositoryName, String suffix, String fileMode) throws FileNotFoundException
	{
		final StringBuilder path = new StringBuilder(parentDirectory.getAbsolutePath());

		if (!path.toString().endsWith(File.separator))
		{
			path.append(File.separator);
		}

		path.append(getFileName(repositoryName, suffix, fileMode));
		this.path = path.toString();

		this.raf = new RandomAccessFile(new File(this.path), fileMode.toString());
		channel = raf.getChannel();
		this.fileMode = FileMode.toFileMode(fileMode);
	}

	public RepositoryFile(File file, FileMode fileMode)	throws FileNotFoundException
	{
		this.raf = new RandomAccessFile(file, fileMode.toString());
		channel = raf.getChannel();
		this.fileMode = fileMode;
		this.path = file.getAbsolutePath();
	}

	private RepositoryFile(SearchPath path, String repositoryName, String suffix, FileMode mode) throws FileNotFoundException
	{
		this(path.search(getFileName(repositoryName, suffix, mode.toString())),	mode);
	}

	private static String getFileName(String fileName, String suffix,	String fileMode)
	{
		return ensureSuffix(fileName.trim(), suffix.trim());
	}

	private static File getFile(String repositoryName, String suffix, String fileMode)
	{
		String fileName = getFileName(repositoryName, suffix, fileMode);
		return new File(fileName);
	}

	private static String ensureSuffix(String fileName, String suffix)
	{
		String dotSuffix = suffix;
		
		if (!suffix.startsWith(DOT_STRING))
		{
			dotSuffix = DOT_STRING + suffix;
		}
		
		if (fileName.endsWith(dotSuffix))
		{
			return fileName;
		}
		
		return fileName + dotSuffix;				
	}

	/**
	 * reads the given number of bytes from the channel into a byte buffer in
	 * memory.
	 * 
	 * @param byteCount
	 * @return
	 * @throws IOException
	 */
	public ByteBuffer read(int byteCount) throws IOException
	{
		ByteBuffer bb = ByteBuffer.allocate(byteCount);
		long nBytes = channel.read(bb);
		assert nBytes == byteCount;
		bb.flip();
		return bb;
	}

	public void close() throws IOException
	{
		logger.debug("closing repository file");

		try
		{
			if (channel != null)
			{
				channel.close();
			}
		}
		finally
		{
			if (raf != null)
			{
				raf.close();
			}
		}
	}

	public MappedByteBuffer map(long startPosition, int byteCount) throws IOException
	{
		return channel.map(fileMode.getMapMode(), startPosition, byteCount);
	}

	public FileMode getFileMode()
	{
		return fileMode;
	}
 
	public long size() throws IOException
	{
		return channel.size();
	}

	@Override
	public String toString()
	{
		return path;
	}
	
	
}

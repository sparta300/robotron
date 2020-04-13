package com.lbg.persist.daemon;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.hydroid.file.DirectoryList;
import org.hydroid.file.PhysicalResourceException;
import org.junit.Test;

import com.google.inject.Injector;
import com.lbg.file.RepositoryFile;
import com.lbg.module.PageDaemonModule;
import com.lbg.module.PropertyModule;
import com.lbg.utility.PropertyMap;

public class RepositoryFileTest
{
	private static final String RESOURCE_DIRECTORY = "src/test/resources";
	private static final String FILE_NAME = "example.ts";
	
	@Test
	public void searchPositive() throws FileNotFoundException
	{
		final DirectoryList searchPath = new DirectoryList(Arrays.asList(new String[] { RESOURCE_DIRECTORY }));
		final File file = searchPath.search(FILE_NAME);
				
		assertTrue(file.getAbsolutePath().endsWith(FILE_NAME));
	}
	
	@Test
	public void searchNegative()
	{
		final DirectoryList searchPath = new DirectoryList(Arrays.asList(new String[] { RESOURCE_DIRECTORY }));

		try
		{
			searchPath.search("non-existent.ts");
			fail();
		}
		catch (FileNotFoundException e)
		{
		}
	}	

	@Test
	public void readFile() throws IOException
	{
		final Injector injector = getInjector();
		final PageDaemon daemon = injector.getInstance(PageDaemon.class);
		final PropertyMap props = injector.getInstance(PropertyMap.class);
		final int blockSize = props.getInteger("block.size");
		final String fileSuffix = props.getString("file.suffix");
		
		final DirectoryList searchPath = new DirectoryList(Arrays.asList(new String[] { RESOURCE_DIRECTORY }));
		final RepositoryFile file = new RepositoryFile(searchPath, FILE_NAME, fileSuffix, "r");
		final PageIdentifier pageId = new PageIdentifier(file, 0, blockSize);

		try
		{
			daemon.pageIn(pageId);
		}
		catch (PageException e)
		{
			fail("page exception on page-in");
		}
		catch (PhysicalResourceException e)
		{
			fail("i/o exception on page-in");
		}
		finally
		{
			try
			{
				file.close();
			}
			catch (IOException e)
			{
				fail("i/o exception closing repo");
			}
		}

	}

	private Injector getInjector()
	{
		// maybe src/test/resources/page-cache.properties
		// maybe page-cache.properties with folder in classpath
		return createInjector(new PropertyModule("src/test/resources/page-cache.properties"), new PageDaemonModule());
	}
}

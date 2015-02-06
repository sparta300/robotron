package org.hydroid.beowulf.model.list;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Iterator;

import org.hydroid.beowulf.RepositoryManager;
import org.hydroid.beowulf.manager.SandpitManager;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.general.ApiContext;
import org.hydroid.beowulf.storage.general.SlotFinder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lbg.file.RepositoryFile;
import com.lbg.persist.BufferBasedObjectSerialiser;
import com.lbg.persist.daemon.PageDaemon;
import com.lbg.persist.daemon.PageException;
import com.lbg.resource.PhysicalResourceException;
import com.lbg.utility.ApplicationContext;
import com.lbg.utility.SpringContextBuilder;

public class SinglyLinkedListTest
{
	private BufferBasedObjectSerialiser serialiser = new BufferBasedObjectSerialiser(5120);
	private ApplicationContext appctx;

	@Before
	public void init()
	{
		SpringContextBuilder builder = new SpringContextBuilder();
		builder.add("test-singly-linked-list.xml");
		builder.add("test-repoman.xml");
		builder.add("test-space.xml");
		appctx = builder.build();
	}

	@Test
	public void testSinglyLinkedList()
	{
		long start = System.currentTimeMillis();
		RepositoryManager repoman = getBean("repoman");
		SlotFinder slotFinder = getBean("slotFinder");
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		ApiContext apiContext = getBean("apiContext");
		ListStorageApi api = null;
		SandpitManager sandpitManager = repoman.getSandpitManager();

		try
		{
			api = new ListStorageApiImpl(apiContext, repoman.getSizing(), slotFinder, 2010);
			PersistentLinkedListModel model = sandpitManager.getLinkedList(api);

			assertTrue(model.isEmpty());

			model.add(serialiser.write(new Integer(0)));
			assertFalse(model.isEmpty());

			Iterator<Locator> it = model.iterate();
			assertData(api, it, "b0s1", 0);

			for (int i = 1; i < 6; i++)
			{
				model.add(serialiser.write(new Integer(i)));
			}

			model.add(serialiser.write(new Integer(6)));

			it = model.iterate();
			assertData(api, it, "b1s3", 6);
			assertData(api, it, "b1s2", 5);
			assertData(api, it, "b1s1", 4);
			assertData(api, it, "b1s0", 3);
			assertData(api, it, "b0s3", 2);
			assertData(api, it, "b0s2", 1);
			assertData(api, it, "b0s1", 0);

		}
		catch (PageException e)
		{
			fail("page exception on page-in");
		}
		catch (PhysicalResourceException pre)
		{
			fail("i/o exception on page-in " + pre.toString());
		}
		finally
		{
			daemon.flushAll();

			try
			{
				repo.close();
			}
			catch (IOException e)
			{
				fail("i/o exception closing repo");
			}
		}

		long finish = System.currentTimeMillis();
		System.out.println(String.format("timing: start=%d finish=%d diff=%d", start, finish, finish - start));
	}

	private void assertData(ListStorageApi api, Iterator<Locator> it, String expectedMnemonic, int expectedDataValue) throws PhysicalResourceException
	{
		Locator dataL = it.next();
		assertEquals(expectedMnemonic, dataL.asAddress());
		byte[] data = api.getSegmentData(dataL.asLong());
		int value = (Integer) serialiser.read(data);
		assertEquals(expectedDataValue, value);
	}

	@After
	public void destroy()
	{
		appctx = null;
	}

	@SuppressWarnings("unchecked")
	private <T> T getBean(String name)
	{
		return (T) appctx.getBean(name);
	}
}

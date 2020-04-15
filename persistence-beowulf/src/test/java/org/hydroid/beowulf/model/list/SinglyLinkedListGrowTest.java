package org.hydroid.beowulf.model.list;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Iterator;

import org.hydroid.beowulf.RepositoryManager;
import org.hydroid.beowulf.manager.SandpitManager;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.general.ApiContext;
import org.hydroid.beowulf.storage.general.SlotFinder;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.PageDaemon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lbg.persist.BufferBasedObjectSerialiser;
import com.lbg.persist.daemon.PageException;
import com.lbg.utility.ApplicationContext;
import com.lbg.utility.SpringContextBuilder;

public class SinglyLinkedListGrowTest {
	
	private BufferBasedObjectSerialiser serialiser = new BufferBasedObjectSerialiser(5120);
	private ApplicationContext appctx;	
	
	@Before
	public void init() {
		SpringContextBuilder builder = new SpringContextBuilder();
		builder.add("test-singly-linked-list.xml");
		builder.add("test-repoman.xml");
		builder.add("test-space.xml");
		appctx = builder.build();
		
	}
	
	@Test
	public void testSinglyLinkedList()  {
		long start = System.currentTimeMillis();
		RepositoryManager repoman = getBean("repoman");
		SlotFinder slotFinder = getBean("slotFinder");
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		ApiContext apiContext = getBean("apiContext");
		ListStorageApi api = null;
		SandpitManager sandpitManager = repoman.getSandpitManager();
				
		try {
			api = new ListStorageApiImpl(apiContext, repoman.getSizing(), slotFinder, 2010);
			PersistentLinkedListModel model = sandpitManager.getLinkedList(api);
						
			assertTrue(model.isEmpty());
			
			
			for (int i = 0; i < 100; i++) {
				model.add(serialiser.write(new Integer(i)));
			}
			
			

			Iterator<Locator> it = model.iterate();
			
			while (it.hasNext()) {
				System.out.println(api.getSegmentData(it.next().asLong()));
			}

						 
			
			
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in " + pre.toString());
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
				
		long finish = System.currentTimeMillis();
		System.out.println(String.format("timing: start=%d finish=%d diff=%d", start, finish, finish - start));
	}

	@After
	public void destroy() {
		appctx = null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getBean(String name) {
		return (T) appctx.getBean(name);
	}
}

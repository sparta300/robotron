package org.hydroid.beowulf.storage.general;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hydroid.beowulf.RepositoryManager;
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

public class GeneralStorageModelTest {

	private BufferBasedObjectSerialiser serialiser = new BufferBasedObjectSerialiser(5120);
	private ApplicationContext appctx;	
	
	@Before
	public void init() {
		SpringContextBuilder builder = new SpringContextBuilder();
		builder.add("test-overlay.xml");
		builder.add("test-repoman.xml");
		builder.add("test-space.xml");
		appctx = builder.build();
		
	}
	
	@Test
	public void testGeneralStorageModel()  {
		long start = System.currentTimeMillis();
		RepositoryManager repoman = getBean("repoman");
		SlotFinder slotFinder = getBean("slotFinder");
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		ApiContext apiContext = getBean("apiContext");
		GeneralStorageApi api = null;
		List<Integer> list = new ArrayList<Integer>(100);
		
		try {
			//StorageManager m1 = repoman.getStorageManager(1L);
			api = new GeneralStorageApiImpl(apiContext, repoman.getSizing(), slotFinder, 2010);
						
			for (int i = 0; i < 100; i++) {
				list.add(new Integer(i));
				api.add(serialiser.write((Serializable)list));
			}
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in" + pre.toString());
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
				
		long finish = System.currentTimeMillis();
		System.out.println(String.format("start=%d finish=%d diff=%d", start, finish, finish - start));
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

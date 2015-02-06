package org.hydroid.beowulf;

import static com.google.inject.Guice.createInjector;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import java.io.File;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.space.SpaceManagementContextFactory;
import org.hydroid.beowulf.space.SpaceManagementContextFactoryImpl;
import org.hydroid.beowulf.storage.Block56Slot4Subslot4;
import org.hydroid.beowulf.storage.Slot;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.inject.Injector;
import com.lbg.file.Directory;
import com.lbg.module.PageDaemonModule;
import com.lbg.module.PropertyModule;
import com.lbg.persist.daemon.PageDaemon;
import com.lbg.persist.daemon.PageDaemonImpl;
import com.lbg.resource.PhysicalResourceException;
import com.lbg.utility.Exceptions;

public class LocationManagerTest {
	private LocationManagerApi locationManager;
	
	@Before
	public void before() {	
		final Directory directory = new Directory(new File("I:/2008/ganymede-ws/persistence-beowulf/rt/repo"));
		final Injector injector = getInjector();
		final PageDaemon pageDaemon = injector.getInstance(PageDaemonImpl.class);
		final SpaceManagementContextFactory factory = new SpaceManagementContextFactoryImpl(new Block56Slot4Subslot4());
		locationManager = new LocationManager("hydroid", directory, ".store", pageDaemon, factory);
	}
	
	@After
	public void after() {
		try {
			locationManager.shutDown();
		} catch (PhysicalResourceException e) {
			e.printStackTrace(System.err);
		}
	}
	
	@Ignore
	public void iterateNames() {
		for (String name : locationManager.iterateStoreNames()) {
			System.out.println(name);
		}		
	}
	
	@Test	
	public void read() {
		RepositoryManager repoman = null;
		
		try {
			repoman = locationManager.read("exop://localhost/singly");
		} catch (PhysicalResourceException e) {
			fail("cannot read store "  + Exceptions.asString(e));
		}
		
		System.out.format("block count: %d%n", repoman.getBlockCount());
		
		try {
			StorageManager sm0 = repoman.getStorageManager(0L);
			
			assertEquals(4, sm0.getSlotCount());
			

			for (Slot slot : sm0.getUsedSlots()) {
				System.out.format("b%ds%d%n", sm0.getBlockId(), slot.getSlotId());
			}
			
		} catch (PhysicalResourceException e) {
			fail("cannot get storage manager " + Exceptions.asString(e));
		}
	
	}
	
	private Injector getInjector()
	{
		return createInjector(new PropertyModule("test/resource/page-cache.properties"), new PageDaemonModule());
	}
}

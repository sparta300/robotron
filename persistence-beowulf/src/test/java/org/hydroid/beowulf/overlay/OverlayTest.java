package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.hydroid.beowulf.overlay.creator.RootBlockCreator;
import org.hydroid.beowulf.overlay.creator.StorageBlockCreator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.hydroid.file.DirectoryList;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;
import com.lbg.utility.ApplicationContext;
import com.lbg.utility.SpringContextBuilder;

public class OverlayTest {
	@Before
	public void init() {
		SpringContextBuilder builder = new SpringContextBuilder();
		builder.add("test-overlay.xml");
		appctx = builder.build();
		locatorFactory = getBean("locatorFactory");
	}
	
	@Ignore
	public void testSearch() throws FileNotFoundException {
		DirectoryList searchPath = getBean("searchPath");
		File file = searchPath.search("play.store");
		assertEquals("I:\\2008\\ganymede-ws\\persistence-beowulf\\rt\\repo\\play.store", file.getAbsolutePath());	
	}
	
	@Ignore
	public void testReadRepo()  {
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		
		PageIdentifier pageId = new PageIdentifier(repo, 0, 1024);
		
		try {
			daemon.pageIn(pageId);
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in");
		} finally {
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}		
		
	}	
	
	@Test
	public void testMakeRepo()  {
		int blockSize = 1024;
		int slotSize = 128;
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		PageIdentifier pageId = new PageIdentifier(repo, 0, blockSize);
		OverlayFactory factory = new OverlayFactory(true, locatorFactory);
		Page page = null;
		
		try {
			page = daemon.make(pageId);
			ByteBuffer bb = page.getByteBuffer();
			
//			List<String> overlayTypes = Arrays.asList("md", "sz", "ro", "sp", "bo");			
//			CompositeOverlay overlay = new CompositeOverlay(bb, overlayTypes, factory);
//			
//
//			int preambleSize = overlay.size();
//			assertEquals(168, preambleSize);
//			
//			SlotOverhead slotOverhead = factory.make("so", bb);
//			int slotOverheadSize = slotOverhead.size();
//			assertEquals(65, slotOverheadSize);
//			
//			BlockOverhead bo = overlay.getComponent("bo");
//
//			int blockOverheadSize = bo.size();
//			assertEquals(12, blockOverheadSize);
//			
//			SlotCountCalculator calculator = new SlotCountCalculator(blockSize, slotSize, preambleSize, blockOverheadSize, slotOverheadSize);
//			assertEquals(4, calculator.getRootBlockSlotCount());
//			assertEquals(5, calculator.getOtherBlockSlotCount());
//			
//			bo.setBlockId(0L);
//			bo.setTotal(calculator.getRootBlockSlotCount());
//			bo.setFree(calculator.getRootBlockSlotCount());
//			bo.setUsed(0);
//			
//
//			bo.end();
//			bb.position(bb.position() - 1);
//			
//			overlay.add(bb, "fsl", factory);
//			
//			Sizing sz = overlay.getComponent("sz");
//			sz.setBlockSize(blockSize);
//			sz.setSlotSize(slotSize);
//			sz.setSlotOverheadSize(slotOverheadSize);
//			sz.setRootBlockSlotCount(calculator.getRootBlockSlotCount());
//			sz.setOtherBlockSlotCount(calculator.getOtherBlockSlotCount());
//			sz.setBlockOverheadSize(blockOverheadSize);
//			sz.setRepositoryOverheadSize(overlay.getComponent("ro").size());
//			sz.setFreeSlotPointerSize(1);
			
			RootBlockCreator root = new RootBlockCreator(bb, factory, locatorFactory, blockSize, slotSize);
			
			System.out.println(root.report());
			
			Sizing sz = root.getSizing();
			
			PageIdentifier pageId1 = new PageIdentifier(repo, 1024, blockSize);
			Page page1 = daemon.make(pageId1);
			ByteBuffer b1 = page1.getByteBuffer();
			StorageBlockCreator storageBlock = new StorageBlockCreator(1, b1, factory, locatorFactory, sz);
			root.getRepositoryOverhead().setNextBlockId(2);
			
			System.out.println(storageBlock.report());
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in");
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
				
	}	
	
	@Ignore
	public void testMakeRootBlockOnly()  {
		int blockSize = 1024;
		int slotSize = 128;
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		PageIdentifier pageId = new PageIdentifier(repo, 0, blockSize);
		OverlayFactory factory = new OverlayFactory(true, locatorFactory);
		Page page = null;
		
		try {
			page = daemon.make(pageId);
			ByteBuffer bb = page.getByteBuffer();
						
			RootBlockCreator root = new RootBlockCreator(bb, factory, locatorFactory, blockSize, slotSize);
			root.report();
						
			assertEmptyRoot(root);			
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in");
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
				
	}
	
	@Ignore
	public void testReadRootBlockUsingOverlay()  {
		int blockSize = 1024;
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		PageIdentifier pageId = new PageIdentifier(repo, 0, blockSize);
		OverlayFactory factory = new OverlayFactory(false, locatorFactory);
		Page page = null;
		
		try {
			page = daemon.pageIn(pageId);
			ByteBuffer bb = page.getByteBuffer();
						
			RootBlock root = new RootBlock(bb, factory, locatorFactory);
			root.report();
						
			assertEmptyRoot(root);			
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in");
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
		
		
				
	}	
	
	@Ignore
	public void testReadRootBlockChunkByChunk()  {
		int blockSize = 1024;		
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repo");
		PageIdentifier pageId = new PageIdentifier(repo, 0, blockSize);
		new OverlayFactory(false, locatorFactory);
		Page page = null;
		
		// 

		try {
			page = daemon.pageIn(pageId);
			ByteBuffer bb = page.getByteBuffer();
						
			new MetaData(bb, locatorFactory);
			new Sizing(bb, locatorFactory);
			new RepositoryOverhead(bb, locatorFactory);
			new Sandpit(bb, locatorFactory);
			new BlockOverhead(bb, locatorFactory);
			bb.position(bb.position() - 1);
			new FreeSlotList(bb, locatorFactory);
			
		} catch (PageException e) {
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			fail("i/o exception on page-in");
		} finally {
			daemon.flushAll();
			
			try {
				repo.close();
			} catch (IOException e) {
				fail("i/o exception closing repo");
			}
		}
				
	}	

	
	private void assertEmptyRoot(RootOverlay root) {
		MetaData md = root.getMetaData();
		assertEquals(16, md.size());
		assertEquals(0xbeefbabe, md.getMagicNumber());
		assertEquals(0, md.getMajorVersion());
		assertEquals(2, md.getMinorVersion());
		assertEquals(1, md.getBuildNumber());
		
		Sizing sz = root.getSizing();
		assertEquals(32, sz.size());
		assertEquals(1024, sz.getBlockSize());
		assertEquals(128, sz.getSlotSize());
		assertEquals(65, sz.getSlotOverheadSize());
		assertEquals(1, sz.getFreeSlotPointerSize());
		assertEquals(24, sz.getRepositoryOverheadSize());
		assertEquals(12, sz.getBlockOverheadSize());
		assertEquals(4, sz.getRootBlockSlotCount());
		assertEquals(5, sz.getOtherBlockSlotCount());
		
		RepositoryOverhead ro = root.getRepositoryOverhead();
		assertEquals(24, ro.size());
		assertEquals(1L, ro.getNextBlockId());
		assertEquals(UNSET_LOCATOR, ro.getTransactionManagerLocator());
		assertEquals(UNSET_LOCATOR, ro.getSpaceManagerLocator());
		
		Sandpit sp = root.getSandpit();
		assertEquals(80, sp.size());
		assertEquals(UNSET_LOCATOR, sp.getLinkedListLocator());
		assertEquals(UNSET_LOCATOR, sp.getListLocator());
		assertEquals(UNSET_LOCATOR, sp.getBagLocator());
		assertEquals(UNSET_LOCATOR, sp.getNameSpaceLocator());
		assertEquals(UNSET_LOCATOR, sp.getBinaryTreeLocator());
		assertEquals(UNSET_LOCATOR, sp.getbPlusTreeLocator());
		assertEquals(UNSET_LOCATOR, sp.getDiaryLocator());
		assertEquals(UNSET_LOCATOR, sp.getTelemetryLocator());
		assertEquals(UNSET_LOCATOR, sp.getHashMapLocator());
		assertEquals(UNSET_LOCATOR, sp.getSourceRepositoryLocator());
		
		BlockOverhead bo = root.getBlockOverhead();
		assertEquals(12, bo.size());
		assertEquals(1, bo.getBlockType().getBlockType());
		assertEquals(0, bo.getBlockId());
		assertEquals(4, bo.getFree());
		assertEquals(0, bo.getUsed());
		assertEquals(4, bo.getTotal());
		
		FreeSlotList fsl = root.getFreeSlotList();
		assertEquals(4, fsl.size());
		
		for (int s = 0; s < 4; s++) {
			assertEquals(s, fsl.getPointer(s));
		}
	}	
	
	@After
	public void destroy() {
		appctx = null;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getBean(String name) {
		return (T) appctx.getBean(name);
	}
	
	private LocatorFactory locatorFactory;	
	private ApplicationContext appctx;
}

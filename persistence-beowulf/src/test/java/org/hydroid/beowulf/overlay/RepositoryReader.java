package org.hydroid.beowulf.overlay;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.hydroid.beowulf.SlotOverlayType;
import org.hydroid.beowulf.StorageManagerContext;
import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.manager.StorageManagerImpl;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHeadSlot;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegmentSlot;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageException;
import org.hydroid.page.PageIdentifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mfdev.utility.SpringContextBuilder;
import com.mfdev.utility.SpringContextBuilderImpl;

public class RepositoryReader {
	private LocatorFactory locatorFactory;
	private ClassPathXmlApplicationContext appctx;
	private final OverlayFactory reader;
	private static final int BLOCK_SIZE = 1024;
	
	private RepositoryReader(String[] args) {
		String ctx = args[0];
		SpringContextBuilder builder = map.get(ctx);
		appctx = builder.build();		
		locatorFactory = getBean("locatorFactory");
		
		assert locatorFactory != null;
		
		reader = new OverlayFactory(false, locatorFactory);
	}
	
	public static void main(String[] args) {
		RepositoryReader reader = new RepositoryReader(args);
		reader.read();
	}

	
	private void read() {
		PageDaemon daemon = getBean("pageDaemon");
		RepositoryFile repo = getBean("repoReadOnly");
				
		try {
			readAll(daemon, repo);
		} catch (PageException e) {
			System.err.println(e);
			fail("page exception on page-in");
		} catch (PhysicalResourceException pre) {
			System.err.println(pre);
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
	
	private void readAll(PageDaemon daemon, RepositoryFile repo) throws PhysicalResourceException {		
		PageIdentifier rootPageId = new PageIdentifier(repo, 0, BLOCK_SIZE);
		Page rootPage = daemon.pageIn(rootPageId);
		daemon.pin(rootPage);
		RootBlock b0 = new RootBlock(rootPage.getByteBuffer(), reader, locatorFactory);
		Sizing sz = b0.getSizing();
		StorageManagerContext context = new StorageManagerContext(reader, reader, sz);
		
		StorageManager m0 = new StorageManagerImpl(context, 0, b0.getBlockOverhead(), b0.getFreeSlotList(), 
                								   b0.getSlotOverheads(),
                								   b0.getBuffers(),
                								   locatorFactory);
		
		
		report(m0);
		
		
		// read root block
		long nextBlockId = b0.getRepositoryOverhead().getNextBlockId();
				
		for (long b = 1; b < nextBlockId; b++) {
			System.out.println(String.format("fetch(b%d)", b));
			PageIdentifier id = new PageIdentifier(repo, b * BLOCK_SIZE, BLOCK_SIZE);
			Page blockPage = daemon.fetch(id);
			StorageBlock block = new StorageBlock(blockPage.getByteBuffer(), reader, locatorFactory, sz);
			
			FreeSlotList fsl = block.getFreeSlotList();
			BlockOverhead bo = block.getBlockOverhead();
			
			
			StorageManager manager = new StorageManagerImpl(context, b, bo, fsl, 
					                                        block.getSlotOverheads(),
					                                        block.getBuffers(),
					                                        locatorFactory);
			
			report(manager);
		
		}
		

	}
	
	private void report(StorageManager manager) {
		for (int s : manager.getFreeSlotManager().findUsedSlots()) {
			SlotOverhead so = manager.getSlotOverhead(s);
			System.out.println("***** b" + manager.getBlockId() + " " + so.toString());
			SlotOverlayType slotOverlayType = so.getSlotOverlayType();
			
			if (slotOverlayType == SlotOverlayType.LL_LIFO_HEAD) {
				ByteBuffer bb = manager.getBuffer(s);
				SinglyLinkedListHeadSlot headSlot = new SinglyLinkedListHeadSlot(bb, reader, locatorFactory);
				System.out.println(headSlot.toString());
				int index = 0;
				int max = headSlot.getRuntime().getLength();
				
//				for (SinglyLinkedListSegment segment : headSlot.getSegments()) {
//					if (index < max) {
//						String dataL = new Locator(segment.getDataLocator()).toMnemonic();
//						
//						long nextL = segment.getNextSegmentLocator();
//						String next = "null";
//						
//						if (nextL != UNSET_LOCATOR) {
//							next = new Locator(nextL).toFullMnemonic();
//						}
//						
//						System.out.println(String.format("[%d] data&=%s next&=%s", index++, dataL, next));
//					}
//				}					
			} else if (slotOverlayType == SlotOverlayType.LL_LIFO_SEGMENT) {
				ByteBuffer bb = manager.getBuffer(s);
				SinglyLinkedListSegmentSlot slot = new SinglyLinkedListSegmentSlot(bb, reader, locatorFactory);
				System.out.println(slot.toString());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T getBean(String name) {
		return (T) appctx.getBean(name);
	}
	
	private static final Map<String, SpringContextBuilder> map = new HashMap<String, SpringContextBuilder>();
	
	static {
		SpringContextBuilder builder = new SpringContextBuilderImpl();
		builder.add("test-reader.xml");
		builder.add("test-reader-config.xml");
		builder.add("test-repoman.xml");
		builder.add("test-space.xml");
		map.put("ctx1", builder);
	}

}

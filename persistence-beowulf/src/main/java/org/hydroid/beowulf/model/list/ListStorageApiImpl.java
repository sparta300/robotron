package org.hydroid.beowulf.model.list;

import static org.hydroid.beowulf.SlotOverlayType.LL_LIFO_HEAD;
import static org.hydroid.beowulf.SlotOverlayType.LL_LIFO_HEAD_TYPE;
import static org.hydroid.beowulf.SlotOverlayType.LL_LIFO_SEGMENT;
import static org.hydroid.beowulf.SlotOverlayType.LL_LIFO_SEGMENT_TYPE;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.model.list.creator.LifoLinkedListCreator;
import org.hydroid.beowulf.model.list.creator.LifoLinkedListSegmentCreator;
import org.hydroid.beowulf.model.list.manager.SinglyLinkedListManager;
import org.hydroid.beowulf.model.list.manager.SinglyLinkedListSegmentManager;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListHeadSlot;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegment;
import org.hydroid.beowulf.model.list.overlay.SinglyLinkedListSegmentSlot;
import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.space.ResponsibilityChain;
import org.hydroid.beowulf.space.SlotRequest;
import org.hydroid.beowulf.space.SpaceRequest;
import org.hydroid.beowulf.space.SpaceRequestImpl;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.general.AddObjectRequest;
import org.hydroid.beowulf.storage.general.ApiContext;
import org.hydroid.beowulf.storage.general.SlotFinder;
import org.hydroid.beowulf.storage.general.SlotSkinImpl;
import org.hydroid.beowulf.storage.general.visitor.CopyVisitor;
import org.hydroid.beowulf.storage.general.visitor.StoreVisitor;
import org.hydroid.file.PhysicalResourceException;
import com.lbg.resource.ResourceNotFound;

public class ListStorageApiImpl implements ListStorageApi {
	public ListStorageApiImpl(ApiContext context,
							  Sizing sizing,
			                  SlotFinder slotFinder, int transactionId)	throws PhysicalResourceException {
		this.sz = sizing;
		this.transactionId = transactionId;
		this.locatorFactory = context.getLocatorFactory();

		chain = context.getChain();
		this.slotFinder = slotFinder;
		slotSkin = new SlotSkinImpl(sizing, slotFinder);
	}
	
	@Override
	public SinglyLinkedListManager newSinglyLinkedList() throws PhysicalResourceException {
		SinglyLinkedListManager manager = newSinglyLinkedListHead();		
		
		// tell the storage manager we are taking the slot
		SinglyLinkedListHeadSlot headSlot = manager.getHeadSlot();		
		
		return new SinglyLinkedListManager(manager.getBlockId(), manager.getSlotOverhead(), headSlot, locatorFactory);
	}
	
	private SinglyLinkedListManager newSinglyLinkedListHead() throws PhysicalResourceException {
		// request the space
		Slot headSlot = allocateSlot();
		SlotOverhead so = headSlot.getSlotOverhead();
		
		// create a slot overlay
		StorageManager storageManager = headSlot.getStorageManager();
		OverlayFactory factory = storageManager.getContext().getForge();		
		ByteBuffer bb = headSlot.getBuffer();
		bb.position(0);	
		new LifoLinkedListCreator(bb, factory, locatorFactory, sz);
		
		// now load it
		bb.position(0);
		SinglyLinkedListHeadSlot linkedListHeadSlot = new SinglyLinkedListHeadSlot(bb, storageManager.getContext().getReader(), locatorFactory);
		
		storageManager.allocateSlot(headSlot.getSlotId(), linkedListHeadSlot.size(), LL_LIFO_HEAD.getTypeId());
		
		return new SinglyLinkedListManager(headSlot.getBlockId(), so, linkedListHeadSlot, locatorFactory);
	}

	private Slot allocateSlot() throws PhysicalResourceException {
		SlotRequest spaceRequest = new SlotRequest(getNextSpaceRequestId(), transactionId, sz);
		chain.handleRequest(spaceRequest);
		Slot headSlot = spaceRequest.getResponse().get(0);
		return headSlot;
	}
	
	@Override
	public SinglyLinkedListManager getManager(long locator) throws PhysicalResourceException {
		// grab the slot overhead and slot buffer
		Slot headSlot = slotFinder.find(locator);
		SlotOverhead so = headSlot.getSlotOverhead();
		
		SinglyLinkedListHeadSlot overlay = overlayHead(headSlot);
		
		return new SinglyLinkedListManager(headSlot.getBlockId(), so, overlay, locatorFactory);
	}
	
	@Override
	public long storeSegmentData(byte[] data) throws PhysicalResourceException {
		AddObjectRequest addRequest = new AddObjectRequest(this.transactionId, data, false);
		SpaceRequest request = new SpaceRequestImpl(addRequest,	getNextSpaceRequestId(), sz);
		chain.handleRequest(request);
		
		StoreVisitor visitor = new StoreVisitor(data, request, locatorFactory);
		slotSkin.accept(visitor);
		long dataL = visitor.getLocator();
		return dataL;
	}	
	
	public byte[] getSegmentData(long locator) throws PhysicalResourceException {
		CopyVisitor visitor = new CopyVisitor(locator);
		slotSkin.accept(visitor);
		return visitor.getObjectData();
	}
		
	@Override
	public SinglyLinkedListSegmentManager newSinglyLinkedListSegment() throws PhysicalResourceException {
		// request the space
		Slot slot = allocateSlot();
		SlotOverhead so = slot.getSlotOverhead();
		
		// create a slot overlay
		StorageManager storageManager = slot.getStorageManager();
		OverlayFactory factory = storageManager.getContext().getForge();		
		ByteBuffer bb = slot.getBuffer();
		bb.position(0);	
		new LifoLinkedListSegmentCreator(bb, factory, locatorFactory, sz);
		
		// now load it
		bb.position(0);
		SinglyLinkedListSegmentSlot linkedListSlot = new SinglyLinkedListSegmentSlot(bb, storageManager.getContext().getReader(), locatorFactory);
		
		storageManager.allocateSlot(slot.getSlotId(), linkedListSlot.size(), LL_LIFO_SEGMENT.getTypeId());
		
		return new SinglyLinkedListSegmentManager(slot.getBlockId(), so, linkedListSlot, locatorFactory);
	}	
	
	public SinglyLinkedListSegment getSegment(long locator)	throws PhysicalResourceException {
		Slot slot = slotFinder.find(locator);
		int index = locatorFactory.make(locator).getIndex();
		int type = slot.getSlotOverhead().getSlotOverlayType().getTypeId();
		
		switch (type) {
			case LL_LIFO_HEAD_TYPE:
				SinglyLinkedListHeadSlot head = overlayHead(slot);
				return head.getSegment(index);
			case LL_LIFO_SEGMENT_TYPE:
				SinglyLinkedListSegmentSlot segment = overlaySegment(slot);
				return segment.getSegment(index);
			default:
				throw new ResourceNotFound();
		}
	}	
	
	private SinglyLinkedListHeadSlot overlayHead(Slot headSlot) {
		ByteBuffer bb = headSlot.getBuffer();
		bb.position(0);	
		
		// read the overlay
		StorageManager storageManager = headSlot.getStorageManager();
		OverlayFactory factory = storageManager.getContext().getReader();
				
		return new SinglyLinkedListHeadSlot(bb, factory, locatorFactory);
	}
	
	private SinglyLinkedListSegmentSlot overlaySegment(Slot slot) {
		ByteBuffer bb = slot.getBuffer();
		bb.position(0);	
		
		// read the overlay
		StorageManager storageManager = slot.getStorageManager();
		OverlayFactory factory = storageManager.getContext().getReader();
				
		return new SinglyLinkedListSegmentSlot(bb, factory, locatorFactory);
	}	

	private int getNextSpaceRequestId() {
		return spaceRequestId++;
	}

    private int spaceRequestId = 1;
    
	private Sizing sz;
	private int transactionId;
	private ResponsibilityChain chain;
	private SlotSkinImpl slotSkin;
	private SlotFinder slotFinder;
	private LocatorFactory locatorFactory;
	
}

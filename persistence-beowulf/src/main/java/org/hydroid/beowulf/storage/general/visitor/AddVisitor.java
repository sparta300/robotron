package org.hydroid.beowulf.storage.general.visitor;

import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;
import java.util.List;

import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.space.SpaceRequest;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.beowulf.storage.general.SlotFinder;

import com.lbg.resource.PhysicalResourceException;


public class AddVisitor extends AbstractSlotVisitor
{
    public AddVisitor(byte[] data, SpaceRequest request, LocatorFactory locatorFactory)
    {
        this.bytes = data;        
        this.locatorFactory = locatorFactory;
        
        //this.request = request;        
        slots = request.getResponse();
        slotCount = slots.size();
        isFragmented = (slotCount > 1);        
        firstSlot = slots.get(0);
        
        // the overall locator is the locator of the first free slot
        setLocator(locatorFactory.make(firstSlot.getBlockId(), firstSlot.getSlotId()).asLong());
    }

    public void visitSingleSlot(Slot slot, Sizing sizing) throws PhysicalResourceException
    {
        write(slot, true, true);        
    }
    
    @Override
    public void prepare(Sizing sizing, SlotFinder slotFinder)
    {
    	super.prepare(sizing, slotFinder);
        tracker = new Tracker(sizing.getSlotSize(), bytes.length);   
    }

    public void visitSlot(Slot slot, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {      
        write(slot, isFirst, isLast);        
    }
    
    protected void write(Slot slot, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        ByteBuffer buffer = slot.getBuffer();
        
        if (buffer.isReadOnly())
        {
            throw new PhysicalResourceException("buffer is read-only");
        }
        
        buffer.put(bytes, tracker.getOffset(), tracker.getChunkSize());
        long nextLocator = UNSET_LOCATOR;
        
        if (!isLast) {
            tracker.move();
            Slot nextSlot = slots.get(currentSlotIndex + 1);
            nextLocator = locatorFactory.make(nextSlot.getBlockId(), nextSlot.getSlotId()).asLong();            
        }
       
        updateSlotOverhead(slot.getSlotOverhead(), nextLocator);
        
        // update the block overhead and free slot list
        BlockOverhead bo = slot.getBlockOverhead();
        FreeSlotList fsl = slot.getFreeSlotList();
        bo.setFree(bo.getFree() - 1);
        bo.setUsed(bo.getUsed() + 1);
        fsl.getPointer(slot.getSlotId()).set(SLOT_USED);
    }

    protected void updateSlotOverhead(SlotOverhead so, long nextLocator) {
        updateMask(new SlotMask(so.getMaskPointer()));
        so.setLocator(getLocator());
        so.setNextLocator(nextLocator);
        so.setTotalRawSize(tracker.getTotalRawSize());
        so.setTotalStoredSize(tracker.getTotalStoredSize());
    }
    
    /**
     * updates the index mask for a slot in line with the fragmentation bits IS_LAST|IS_FIRST|IS_FRAGMENTED
     * @param mask
     */
    protected void updateMask(SlotMask mask) {
        if (isFragmented) {
            mask.setFragmented(true);
        }
        
        if (isFragmented && isFirstFragment()) {
            mask.setFirstFragment(true);
        }            
        
        if (isFragmented && isLastFragment()) {
            mask.setLastFragment(true);
        }  
    }
    
    private boolean isLastFragment()
    {
        return isLastFragment(null);
    }

    private boolean isFirstFragment()
    {
        return currentSlotIndex == 0;
    }

    @Override
    protected Slot getFirstSlot() throws PhysicalResourceException
    {
        return firstSlot;
    }

    @Override
    protected Slot getNextSlot(Slot currentSlot) throws PhysicalResourceException
    {      
        return slots.get(++currentSlotIndex);
    }

    @Override
    protected boolean isFragmented(Slot firstSlot)
    {
        return isFragmented;
    }

    @Override
    protected boolean isLastFragment(Slot currentSlot)
    {
        return currentSlotIndex == (slotCount - 1); 
    }
    
    private int slotCount;
    
    boolean isFragmented;
    
    private byte[] bytes;
    private List<Slot> slots;

    private Slot firstSlot;
    private int currentSlotIndex;
    private Tracker tracker;
    private LocatorFactory locatorFactory;
    
}

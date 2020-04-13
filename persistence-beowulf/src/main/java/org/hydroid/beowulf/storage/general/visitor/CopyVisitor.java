package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.file.PhysicalResourceException;

import com.lbg.resource.ResourceRemoved;

public class CopyVisitor extends AbstractSlotVisitor implements SlotMaskVisitor, FirstSlotChecker
{
    public CopyVisitor(long locator) {
        super(locator);
    }
    
    @Override
    public void start(Slot firstSlot, Sizing sizing) throws PhysicalResourceException
    {
        checkFirstSlot(firstSlot);
        
        data = new byte[firstSlot.getSlotOverhead().getTotalRawSize()];
        remaining = data.length;
        offset = 0;   
        slotSize = sizing.getSlotSize();
    }
       
    public void checkFirstSlot(Slot firstSlot) throws PhysicalResourceException
    {
        if (SlotMask.isRemoved(firstSlot.getSlotOverhead().getMask()))
        {
            throw new ResourceRemoved("object with locator " + firstSlot.getLocator() + " has been removed.  Recover using revive() or reanimate()");
        }        
    }

    public void visitSingleSlot(Slot firstSlot, Sizing sizing) throws PhysicalResourceException
    {
        checkFirstSlot(firstSlot);
        
        visitSingleSlotMask(new SlotMask(firstSlot.getSlotOverhead().getMaskPointer()));
        
        data = new byte[firstSlot.getSlotOverhead().getTotalRawSize()];
        int remaining = data.length;

        firstSlot.getBuffer().position(0).limit(remaining);
        firstSlot.getBuffer().get(data, 0, remaining);
    }



    public void visitSlot(Slot slot, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        visitSlotMask(new SlotMask(slot.getSlotOverhead().getMaskPointer()), isFirst, isLast);
        
        if (remaining > slotSize)
        {
            slot.getBuffer().position(0).limit(slotSize);
            slot.getBuffer().get(data, offset, slotSize);
        }
        else
        {
            slot.getBuffer().position(0).limit(remaining);
            slot.getBuffer().get(data, offset, remaining);
        }    
        
        // keep the counters up to date
        remaining -= slotSize;
        offset += slotSize; 
    }

    public byte[] getObjectData()
    {
        return data;
    }    

    private byte[] data;
    private int remaining;
    private int offset;
    private int slotSize;
    
    public void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        // no-op        
    }

    public void visitSingleSlotMask(SlotMask mask) throws PhysicalResourceException
    {
        // no-op
    }
}

package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;

import com.lbg.resource.PhysicalResourceException;
import com.lbg.resource.ResourceNotRemoved;

public class ReviveVisitor extends AbstractIndexVisitor
{
    public ReviveVisitor(long locator)
    {
        super(locator);
    }
    
    public void checkFirstSlot(Slot firstSlot) throws PhysicalResourceException
    {
        if (!(SlotMask.isRemoved(firstSlot.getSlotOverhead().getMask())))
        {
            throw new ResourceNotRemoved("cannot revive object with locator " + firstSlot.getLocator() + "L: this resource has not been removed");
        }
    }

    public void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        mask.setRemoved(false);
    }

    public void visitSingleSlotMask(SlotMask mask) throws PhysicalResourceException
    {
        mask.setRemoved(false);        
    }

}

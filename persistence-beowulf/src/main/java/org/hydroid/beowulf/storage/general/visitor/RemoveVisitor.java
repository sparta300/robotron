package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;

import com.lbg.resource.PhysicalResourceException;
import com.lbg.resource.ResourceAlreadyRemoved;

/**
 * a slot visitor that implements the functionality of {@link PxRepository#remove(long)}.  It implements a logical delete, so all
 * we need to is mark all the fragments as deleted.  Up until the point in time when the space taken up by this object is reclaimed,
 * you can recover the object using {@link PxRepository#revive(long)} or {@link PxRepository#reanimate(long)}.
 * 
 * @author smiley
 */
public class RemoveVisitor extends AbstractIndexVisitor
{    
    public RemoveVisitor(long locator)
    {
        super(locator);
    }
    
    public void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        mask.setRemoved(true);        
    }

    public void checkFirstSlot(Slot headSlot) throws PhysicalResourceException
    {
        if (SlotMask.isRemoved(headSlot.getSlotOverhead().getMask()))
        {
            throw new ResourceAlreadyRemoved("locator " + headSlot.getLocator() + "L");
        }
    }

    public void visitSingleSlotMask(SlotMask mask) throws PhysicalResourceException
    {
        mask.setRemoved(true);
    }
}

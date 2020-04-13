package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.file.PhysicalResourceException;



/**
 * a specialisation of {@link AbstractSlotVisitor} for subclasses that are only interested in modifying the index mask of each slot.
 * 
 * @author smiley
 */
abstract public class AbstractIndexVisitor extends AbstractSlotVisitor implements SlotMaskVisitor, FirstSlotChecker
{
    public AbstractIndexVisitor(long locator)
    {
        super(locator);
    }
    
    public void visitSingleSlot(Slot firstSlot, Sizing sizing) throws PhysicalResourceException
    {
        checkFirstSlot(firstSlot);
        visitSingleSlotMask(new SlotMask(firstSlot.getSlotOverhead().getMaskPointer()));
    }

    /** {@inheritDoc} */
    public void visitSlot(Slot slot, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        if (isFirst)
        {
            checkFirstSlot(slot);
        }
        
        visitSlotMask(new SlotMask(slot.getSlotOverhead().getMaskPointer()), isFirst, isLast);        
    }
    
}

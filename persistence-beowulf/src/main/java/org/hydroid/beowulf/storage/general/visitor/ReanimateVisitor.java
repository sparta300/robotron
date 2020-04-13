package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.file.PhysicalResourceException;

public class ReanimateVisitor extends CopyVisitor
{
    public ReanimateVisitor(long locator)
    {
        super(locator);
    }
    
    @Override
    public void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast) throws PhysicalResourceException
    {
        mask.setRemoved(false);
    }

    @Override
    public void visitSingleSlotMask(SlotMask mask) throws PhysicalResourceException
    {
        mask.setRemoved(false);
    }

}

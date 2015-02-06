package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.SlotMask;


/**
 * a slot visitor that implements the functionality of {@link PxRepository#fetch(long)}.  It implements a logical delete, so all
 * we need to is mark all the fragments as deleted.  Up until the point in time when the space taken up by this object is reclaimed,
 * you can recover the object using {@link PxRepository#revive(long)} or {@link PxRepository#reanimate(long)}.
 * 
 * @author smiley
 */
public class FetchVisitor extends CopyVisitor
{    
    public FetchVisitor(long locator) {
        super(locator);
    }
    
    @Override
    public void visitSingleSlotMask(SlotMask mask)
    {
        mask.setRemoved(true);
    }
    
    @Override    
    public void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast)
    {
        mask.setRemoved(true);        
    }

}

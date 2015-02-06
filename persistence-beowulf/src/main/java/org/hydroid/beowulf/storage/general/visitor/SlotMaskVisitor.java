package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.storage.SlotMask;

import com.lbg.resource.PhysicalResourceException;

public interface SlotMaskVisitor
{
    /**
     * visits the slot mask of the single slot that makes up this serialised object
     * @param mask the index mask of the slot 
     * @throws PhysicalResourceException when anything goes wrong during the visit
     */
    void visitSingleSlotMask(SlotMask mask) throws PhysicalResourceException;
    
    /**
     * visits the slot mask of each slot.  Notice that isFirst and isLast can never be true at the same time
     * because this method is not called for an object that fits into one slot.
     * 
     * @param mask the index mask object
     * @param isFirst true when this is the first slot of a fragmented serialised object
     * @param isLast true when this is the last slot of a fragmented object
     * @throws PhysicalResourceException when anything goes wrong during the visit
     */
    void visitSlotMask(SlotMask mask, boolean isFirst, boolean isLast) throws PhysicalResourceException;
}

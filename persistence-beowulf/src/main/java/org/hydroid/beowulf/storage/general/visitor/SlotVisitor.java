package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.general.SlotFinder;
import org.hydroid.beowulf.storage.general.SlotSkin;
import org.hydroid.file.PhysicalResourceException;

public interface SlotVisitor
{
    void visit(SlotSkin skin) throws PhysicalResourceException;
    
    void prepare(Sizing sizing, SlotFinder slotFinder);
    
    void start(Slot firstSlot, Sizing sizing)
    throws PhysicalResourceException;
    
    void visitSingleSlot(Slot slot, Sizing sizing)
    throws PhysicalResourceException;
    
    void visitSlot(Slot slot, boolean isFirst, boolean isLast)
    throws PhysicalResourceException;
    
    void finish()
    throws PhysicalResourceException;
}

package org.hydroid.beowulf.storage.general.visitor;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.beowulf.storage.general.SlotFinder;
import org.hydroid.beowulf.storage.general.SlotSkin;
import org.hydroid.file.PhysicalResourceException;



abstract public class AbstractSlotVisitor implements SlotVisitor
{    
    /**
     * default constructor needed for slot visitors that don't read from a locator
     */
    public AbstractSlotVisitor() {        
    }
    
    public AbstractSlotVisitor(long locator)
    {
        this.locator = locator;
    }
    
    public void prepare(Sizing sizing, SlotFinder slotFinder) 
    {
    	sz = sizing;
    	this.slotFinder = slotFinder;
    }
    
    public void start(Slot headSlot, Sizing sizing) throws PhysicalResourceException
    {        
    }
    
    public void finish() throws PhysicalResourceException
    {
    }
    
    public long getLocator() {
        return locator;
    }
    
    public void visit(SlotSkin skin) throws PhysicalResourceException
    {        
    	Sizing sz = skin.getSizing();
        prepare(sz, skin.getSlotFinder());
        Slot firstSlot = getFirstSlot();
                    
        if (!isFragmented(firstSlot))
        {
            visitSingleSlot(firstSlot, sz);
            return;
        }
                      
        Slot slot = firstSlot;
        start(firstSlot, sz);
        visitSlot(firstSlot, true, false);
        
        // spin through all of the slots
        while (!isLastFragment(slot))
        {            
            // get the next slot
            slot = getNextSlot(slot);
            
            // visit the current slot
            visitSlot(slot, false, isLastFragment(slot));      
        }        
        
        finish();
    }

    protected boolean isFragmented(Slot firstSlot) {
        return SlotMask.isFragmented(firstSlot.getSlotOverhead().getMask());
    }
    
    protected boolean isLastFragment(Slot currentSlot) {
        return SlotMask.isLastFragment(currentSlot.getSlotOverhead().getMask());
    }
    
    protected Slot getFirstSlot() throws PhysicalResourceException {
        return slotFinder.find(locator);
    }

    protected Slot getNextSlot(Slot currentSlot) throws PhysicalResourceException {
        return slotFinder.find(currentSlot.getSlotOverhead().getNextLocator());
    }
    
    protected Sizing getSizing() { return sz;  }
    protected SlotFinder getSlotFinder() { return slotFinder; }
    protected void setLocator(long locator) { this.locator = locator; }

    private SlotFinder slotFinder;
    private Sizing sz; 
    private long locator;
}

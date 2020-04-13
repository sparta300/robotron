package org.hydroid.beowulf.storage.general;

import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;
import java.util.List;

import org.hydroid.beowulf.overlay.BlockOverhead;
import org.hydroid.beowulf.overlay.FreeSlotList;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.overlay.SlotOverhead;
import org.hydroid.beowulf.space.SpaceRequest;
import org.hydroid.beowulf.storage.Locator;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.beowulf.storage.SlotMask;
import org.hydroid.file.PhysicalResourceException;

/**
 * writes a space request into the store using the slots that have been allocated for it.
 * @author smiley
 */
public class ScatteringWriter
{
    public ScatteringWriter(Sizing sizing, LocatorFactory locatorFactory)
    {
        this.sizing = sizing;
        this.locatorFactory = locatorFactory;
    }
    
    public void write(byte[] bytes, SpaceRequest request) throws PhysicalResourceException
    {
        // these are the target slots
        List<Slot> slots = request.getResponse();
        
        int offset = 0;
        int slotCount = slots.size();
        int slotSize = sizing.getSlotSize();
        int remaining = bytes.length;  
        
        // sizing totals - helps us allocate enough space for the deserialised object
        int totalRawSize = remaining;
        
        // this will only be different if we compress or encode the object in some way
        int totalStoredSize = totalRawSize;
        
        // flags used in setting the various flags on an fragmented object
        boolean isFragmented = (slotCount > 1 ? true : false);
        boolean isFirstFragment = (isFragmented ? true : false);
        Locator locator = null;
                
        for (int s = 0; s < slotCount; s++, offset += slotSize)
        {
            // get the block and the slot and the respective IDs
            Slot freeSlot = slots.get(s);
            int slotId = freeSlot.getSlotId();
            long blockId = freeSlot.getBlockId();
            
            // get access to the next slot
            Slot nextSlot = null;
            long nextLocator = UNSET_LOCATOR;
                        
            try
            {
                nextSlot = slots.get(s + 1);
                nextLocator = locatorFactory.make(nextSlot.getBlockId(), nextSlot.getSlotId()).asLong();
            } catch (IndexOutOfBoundsException e)
            {            
                // keep unset value
            }                        
            
            // get access to the index and its mask bits
            SlotOverhead so = freeSlot.getSlotOverhead();
            SlotMask mask = new SlotMask(so.getMaskPointer());
            
            // the entire object will take the locator of the first slot
            if (locator == null)
            {
                locator = locatorFactory.make(blockId, slotId);
            }
            
            if (isFragmented && isFirstFragment)
            {
                mask.setFirstFragment(isFirstFragment);
                isFirstFragment = false;
            }
            
            if (isFragmented)
            {
                mask.setFragmented(true);
            }
            
            int byteCount = slotSize;
            
            if (remaining < slotSize)
            {
                byteCount = remaining;
                
                if (isFragmented)
                {
                    mask.setLastFragment(true);
                }
            }

            // get access to the buffer in the slot
            ByteBuffer bb = freeSlot.getBuffer();
            
            if (bb.isReadOnly())
            {
                throw new PhysicalResourceException("buffer is read-only");
            }
            
            bb.put(bytes, offset, byteCount);
            remaining -= slotSize;                        
            
            // update the information in the index
            so.setLocator(locator.asLong());
            so.setNextLocator(nextLocator);
            so.setTotalRawSize(totalRawSize);
            so.setTotalStoredSize(totalStoredSize);
            
            // update the block control data
            BlockOverhead bo = freeSlot.getBlockOverhead();
            FreeSlotList fsl = freeSlot.getFreeSlotList();
            bo.setFree(bo.getFree() - 1);
            bo.setUsed(bo.getUsed() + 1);
            fsl.getPointer(slotId).set(SLOT_USED);
        }     
        
        // CAREFUL - commented out - not sure whether ScatteringWriter is still used
        //request.setLocator(locator.toLong());
        
        
    }

    
    private LocatorFactory locatorFactory;
    private Sizing sizing;
}

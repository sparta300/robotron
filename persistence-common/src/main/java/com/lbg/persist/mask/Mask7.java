package com.lbg.persist.mask;

import com.lbg.persist.Unset;
import com.lbg.persist.pointer.BytePointer;

/**
 * a 7 bit mask.
 * 
 * @author C006011
 */
public class Mask7
{
    public static final int IS_ALLOCATED        = 0x01;
    
    private final BytePointer pointer;
	 
	public Mask7(BytePointer pointer)
	{
		this.pointer = pointer;
	}
	
    public boolean isAllocated()
    {
    	final int value = pointer.get();
    	
    	// if the value is not set, we will have to say false
    	if (value == Unset.BOOLEAN_BYTE)
    	{
    		return false;
    	}
    	
        return (pointer.get() & IS_ALLOCATED) > 0;
    }
    
    public void setAllocated(boolean set)
    {
        int mask = pointer.get();
        
        if (mask == Unset.BOOLEAN_BYTE)
        {
        	mask = 0;
        }
        
        if (set)
        {
            mask |= IS_ALLOCATED;
        } 
        else
        {
            mask &= ~IS_ALLOCATED;
        }
        
        pointer.set(mask);
    }
}

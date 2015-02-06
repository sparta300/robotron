package org.hydroid.beowulf.storage;


import static org.hydroid.beowulf.BeowulfConstants.RELEVANT_MASK_BIT_COUNT;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.IntPointer;

public class SlotMask
{
    public SlotMask(IntPointer maskPointer)
    {
        this.maskPointer = maskPointer;
    }
    
    public static boolean isFragmented(int mask)
    {
        return (mask & IS_FRAGMENTED) > 0;
    }
    
    public static boolean isLastFragment(int mask)
    {
        return (mask & IS_LAST_FRAGMENT) > 0;
    }  
    
    public static boolean isFirstFragment(int mask)
    {
        return (mask & IS_FIRST_FRAGMENT) > 0;
    }     
    
    public static boolean isRemoved(int mask)
    {
        return (mask & IS_REMOVED) > 0;
    } 

    public static boolean hasOverlay(int mask)
    {
        return (mask & HAS_OVERLAY) > 0;
    }     
    
    public static String toString(int mask)
    {
    	final StringBuilder buf = new StringBuilder(64);
        
        for (int i = 63; i > -1; i--)
            if (i < RELEVANT_MASK_BIT_COUNT)
            {
                buf.append((mask & (1L << i)) > 0L ? "1" : "0");
            }
        
        buf.append(" (hex=" + Long.toHexString(mask) + ")");
        return buf.toString();
    }
    
    public static List<String> toNameList(int mask)
    {
    	List<String> names = new ArrayList<String>(RELEVANT_MASK_BIT_COUNT);
        
        for (int i = 63; i > -1; i--)
            if (i < RELEVANT_MASK_BIT_COUNT)
            {
            	if ((mask & (1L << i)) > 0L) {
            		names.add(SlotMask.names.get(i));
            	}
            }
        
        return names;
    }
    
    public static String toNameString(int mask) {
    	List<String> list = toNameList(mask);
    	
    	if (list.size() == 0) {
    		return "";
    	}
    	
    	StringBuilder sb = new StringBuilder(list.remove(0));
    	
    	while (list.size() > 0) {
    		sb.append(" | ").append(list.remove(0));
    	}
    	
    	return sb.toString();
    }
    
    public boolean isFragmented()
    {
        return (maskPointer.get() & IS_FRAGMENTED) > 0;
    }
    
    public boolean isLastFragment()
    {
        return (maskPointer.get() & IS_LAST_FRAGMENT) > 0;
    }  
    
    public boolean isFirstFragment()
    {
        return (maskPointer.get() & IS_FIRST_FRAGMENT) > 0;
    }     
    
    public boolean isRemoved()
    {
        logger.debug(toString(maskPointer.get()));
        return (maskPointer.get() & IS_REMOVED) > 0;
    }   
    
    public boolean hasOverlay()
    {
        logger.debug(toString(maskPointer.get()));
        return (maskPointer.get() & HAS_OVERLAY) > 0;
    }        

    public void setFragmented(boolean set)
    {
        int mask = maskPointer.get();
        
        if (set)
            mask |= IS_FRAGMENTED;
        else
            mask &= ~IS_FRAGMENTED;
        
        maskPointer.set(mask);
    }

    public void setFirstFragment(boolean set)
    {
        int mask = maskPointer.get();
        
        if (set)
            mask |= IS_FIRST_FRAGMENT;
        else
            mask &= ~IS_FIRST_FRAGMENT;
        
        maskPointer.set(mask);
    }

    public void setLastFragment(boolean set)
    {
        int mask = maskPointer.get();
        
        if (set)
            mask |= IS_LAST_FRAGMENT;
        else
            mask &= ~IS_LAST_FRAGMENT;
        
        maskPointer.set(mask);
    }
    

    public void setRemoved(boolean set)
    {
        int mask = maskPointer.get();
        
        if (set)
            mask |= IS_REMOVED;
        else
            mask &= ~IS_REMOVED;
        
        maskPointer.set(mask);
    }    
    
	public void setHasOverlay(boolean set) {
        int mask = maskPointer.get();
        
        if (set)
            mask |= HAS_OVERLAY;
        else
            mask &= ~HAS_OVERLAY;
        
        maskPointer.set(mask);
	}    
    
    private IntPointer maskPointer;
    
    public static final int IS_FRAGMENTED        = 0x01;
    public static final int IS_FIRST_FRAGMENT    = 0x02;
    public static final int IS_LAST_FRAGMENT     = 0x04;
    public static final int IS_REMOVED           = 0x08;  
    public static final int HAS_OVERLAY          = 0x10;
    
    private static final List<String> names = new ArrayList<String> (RELEVANT_MASK_BIT_COUNT);
    
    static {
    	names.add("IS_FRAGMENTED");
    	names.add("IS_FIRST_FRAGMENT");
    	names.add("IS_LAST_FRAGMENT");
    	names.add("IS_REMOVED");
    	names.add("HAS_OVERLAY");    	    	    	    	
    }
    
    private static final Logger logger = LoggerFactory.getLogger(SlotMask.class);


}

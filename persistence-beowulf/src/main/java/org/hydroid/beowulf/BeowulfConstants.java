package org.hydroid.beowulf;

public final class BeowulfConstants {
    /**
     * used at the beginning of the repository file to identify it as a repository
     */
    public static final int MAGIC_NUMBER = 0xbeefbabe;
    
    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 3;
    public static final int BUILD_NUMBER = 1;
    
    public static final byte SLOT_USED 			= (byte)121;
    public static final int NO_FREE_SLOT 		= -120;
    
    public static final long UNSET_LOCATOR          = -10L;
    public static final int UNSET_SIZE 			    = -11;
    public static final int UNSET_COUNT 		    = -12;
    public static final long UNSET_OBJECT_TYPE 	    = -13;
    public static final int UNSET_SLOT_INDEX 	    = -14;
    public static final int UNSET_POINTER           = -15;
    public static final int UNSET_BLOCK_ID          = -16;
    public static final int UNSET_SLOT_OVERLAY_TYPE = -17;
    public static final int UNSET_VERSION           = -18;
    
    public static final int UNSET_MASK 			= 0;
    public static final long UNSET_POSITION = 0L;
    
    public static final int UNSET_INDEX = -1;
    
	// Design decision DD-001/2020
    public static final byte BYTE_DEFAULT_VALUE = (byte)0;
    
    /**
     * the current number of bits that are being used in the index mask
     */
    public static final int RELEVANT_MASK_BIT_COUNT = 5;
    
    public static final int[] EMPTY_INT_ARRAY = new int[0];    
}

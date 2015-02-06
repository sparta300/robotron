package com.lbg.utility;

public class BaseUtils
{
	
    /**
     * All possible chars for representing a number as a String
     */
    private final static char[] digits = 
    	{
			'0' , '1' , '2' , '3' , '4' , '5' ,
			'6' , '7' , '8' , '9' , 'a' , 'b' ,
			'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
			'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
			'o' , 'p' , 'q' , 'r' , 's' , 't' ,
			'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    	};

	public BaseUtils()
	{
		// TODO Auto-generated constructor stub
	}
	
    public static String toBinaryString(int i) 
    {
    	return toUnsignedString(i, 1);
    }
    
    public static String toOctalString(int i) 
    {
    	return toUnsignedString(i, 3);
    }
    
    public static String toHexString(int i)
    {
    	return toUnsignedString(i, 4);
    }      

	/**
	 * Convert the integer to an unsigned number.
	 */
	private static String toUnsignedString(int i, int shift)
	{
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		
		do
		{
			buf[--charPos] = digits[i & mask];
			i >>>= shift;
		}
		while (i != 0);
		
		return new String(buf, charPos, (32 - charPos));
	}
}

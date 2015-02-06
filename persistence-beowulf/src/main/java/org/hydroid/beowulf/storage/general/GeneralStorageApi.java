package org.hydroid.beowulf.storage.general;

import com.lbg.resource.PhysicalResourceException;

public interface GeneralStorageApi {
    public void add(byte[] bytes)
    throws PhysicalResourceException;
    
    public long store(byte[] bytes)
    throws PhysicalResourceException;

	public byte[] copy(long locator)
	throws PhysicalResourceException;   
	
	public void duplicate(long locator)
	throws PhysicalResourceException; 	
	
	public void remove(long locator)
	throws PhysicalResourceException; 
	
	public byte[] fetch(long locator)
	throws PhysicalResourceException;
	
	public void replace(long locator, byte[] replacementBytes)
	throws PhysicalResourceException;
	
	public byte[] substitute(long locator, byte[] replacementBytes)
	throws PhysicalResourceException; 	
    
    public void redirect(long locator)
    throws PhysicalResourceException;    

    public byte[] revert(long locator)
    throws PhysicalResourceException;
    
    public void revive(long locator)
    throws PhysicalResourceException;

    public byte[] reanimate(long locator)
    throws PhysicalResourceException;
    
    public void flush() 
    throws PhysicalResourceException;

    public void close() 
    throws PhysicalResourceException;
}

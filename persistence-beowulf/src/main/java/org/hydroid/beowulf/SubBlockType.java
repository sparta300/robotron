package org.hydroid.beowulf;

/**
 * an enumeration of the different types of structures that are smaller than a block
 * 
 * @author C006011
 */
public enum SubBlockType
{
	WILDERNESS(1),
	STORAGE(2),	
	END_MARKER(100),
	;
	
	private final int id;
	
	private SubBlockType(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}
}

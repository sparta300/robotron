package com.lbg.persist.structure;

/**
 * allows you to address a composite structure where the parts can be addressed using an index, such as a list or an array.
 * 
 * @author C006011
 *
 * @param <M> the type of the management object
 * @param <E> the type of the element object
 */
public interface IndexableStructure<M,E>
{
	M getManagement(int index);
	E getElement(int index);
	
	void setManagement(int index, M value);
	void setElement(int index, E element);
}


package org.hydroid.beowulf.model.list;

import java.util.Iterator;

import org.hydroid.beowulf.storage.Locator;

import com.lbg.resource.PhysicalResourceException;

/**
 * a server-side persistent linked list interface.
 * 
 * @author smiley
 */
public interface PersistentLinkedListModel {
	boolean isEmpty() throws PhysicalResourceException;
	int size() throws PhysicalResourceException;
	void add(byte[] item) throws PhysicalResourceException;
	void remove() throws PhysicalResourceException;
	void removeAll() throws PhysicalResourceException;
	byte[] take() throws PhysicalResourceException;
	byte[] peek() throws PhysicalResourceException;
	Iterator<Locator> iterate() throws PhysicalResourceException;
	Locator getMetaData() throws PhysicalResourceException;
	void setMetaData(Locator locator) throws PhysicalResourceException;
	
	/**
	 * get the segment data for the given locator.
	 * 
	 * @param locator
	 * @return
	 * @throws PhysicalResourceException
	 */
	byte[] getSegmentData(Locator locator) throws PhysicalResourceException;
}

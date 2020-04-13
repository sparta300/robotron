package org.hydroid.beowulf.space;

import java.util.List;
import java.util.concurrent.Callable;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;



public interface SpaceManager extends StorageListener, RequestHandler {

	/**
	 * this is the notification method called by the repository manager whenever it creates a storage manager.
	 * @param manager
	 */
	void notify(StorageManager manager);
	
	/**
	 * allows slot listeners to register with the space manager to be notified of available slots.
	 * @param listener the listener to register
	 */
	void register(StorageListener listener);
	
	/**
	 * allows slot listeners to deregister from the space manager, to stop notifications of available slots.
	 * @param listener the listener to deregister
	 */	
	void deregister(StorageListener listener);

	/**
	 * create more space.
	 * @throws PhysicalResourceException 
	 */
	List<Slot> forge() throws PhysicalResourceException;

	/**
	 * a call-back provided by the repository manager to make it possible to request new space to be forged.
	 * @param callable
	 */
	void setForgeCallback(Callable<StorageManager> callable);

	/**
	 * sets the chain of responsiblity which links together all the required request handlers.
	 * 
	 * @param requestHandlerChain
	 */
	void setChain(ResponsibilityChain requestHandlerChain);

}

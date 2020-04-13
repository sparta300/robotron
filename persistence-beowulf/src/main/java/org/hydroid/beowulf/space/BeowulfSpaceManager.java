package org.hydroid.beowulf.space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

import org.hydroid.beowulf.manager.StorageManager;
import org.hydroid.beowulf.storage.Slot;
import org.hydroid.file.PhysicalResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeowulfSpaceManager implements SpaceManager {
	
	public BeowulfSpaceManager() {
		super();
	}
	
	public void notify(StorageManager storageManager) {
		long blockId = storageManager.getBlockId();
		
		if (!broadcasting) {
			logger.debug(String.format("notifyListeners(b%d) not broadcasting, so adding to backlog", blockId));
			backlog.addAll(storageManager.drainFreeSlots());
			return;
		}
		
		List<Slot> slots = new ArrayList<Slot>();
		
		// drain any slots from the backlog first
		if (broadcasting) {
			logger.debug(String.format("notifyListeners(%d) draining backlog", blockId));
			slots.addAll(drainBacklog());
		}		
        
		// now drain all free slots from the storage
		slots.addAll(storageManager.drainFreeSlots());
		
		broadcastSlots(slots);        
	}

	private void broadcastSlots(List<Slot> slots) {
		if (!broadcasting) {
			logger.warn("broadcastSlots() called when not broadcasting");
			backlog.addAll(slots);
			return;
		}
		
		int slotCount = slots.size();
		logger.debug(String.format("broadcastSlots() broadcasting %d slot%s to listeners", slotCount, slotCount != 1 ? "s" : ""));      
                        
        for (Slot slot : slots) {
			notifyListeners(slot);
        }
	}

	private void notifyListeners(Slot slot) {
		if (!broadcasting) {
			logger.debug(String.format("add b%ds%d notification to backlog", slot.getBlockId(), slot.getSlotId()));
			backlog.add(slot);
			return;
		}		
		
		for (StorageListener listener : listeners) {
			boolean accepted = listener.offer(slot);
			
			if (accepted) {
				break;
			}
		}
	}
		
	public boolean offer(Slot slot) {
		if (!broadcasting) {
			logger.debug(String.format("offer(b%ds%d) adding to backlog", slot.getBlockId(), slot.getSlotId()));
			backlog.add(slot);
			return false;
		}
		
		notifyListeners(slot);
		return false;
	}	
	
	/**
	 * allows an listener object to say that it wants to receive notifications of the loading of storage units.
	 */
	public void register(StorageListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
			broadcasting = true;
		}			
	}
	
	/**
	 * clears the backlog of notifications that were kept because nobody was listening.
	 */
	private List<Slot> drainBacklog() {
		int count = backlog.size();
		
		if (count == 0) {
			return Collections.emptyList();
		}
		
		List<Slot> list = new ArrayList<Slot>(count);
		list.removeAll(backlog);
		return list;
	}

	public void deregister(StorageListener listener) {
		listeners.remove(listener);
	}	
	
	@Override
	public List<Slot> forge() throws PhysicalResourceException {
		try { 
			return callback.call().drainFreeSlots();
		} catch (Exception e) {
			throw new PhysicalResourceException("could not obtain new slots", e);
		}
	}

	@Override
	public void handleRequest(SpaceRequest request) throws PhysicalResourceException {
		chain.handleRequest(request);
	}

	@Override
	public void setSuccessor(RequestHandler successor) {
		// ignore
	}	
	
	public RequestHandler getSuccessor() {
		return null;
	}
	
	@Override
	public void setForgeCallback(Callable<StorageManager> callback) {
		this.callback = callback;		
	}
	
	@Override
	public void setChain(ResponsibilityChain requestHandlerChain) {
		chain = requestHandlerChain;	
	}	
	
	private List<Slot> backlog = new ArrayList<Slot>();	
	
	private static final Logger logger = LoggerFactory.getLogger(BeowulfSpaceManager.class);

	private ResponsibilityChain chain;
	private List<StorageListener> listeners = new ArrayList<StorageListener>();	
	private boolean broadcasting;

	private Callable<StorageManager> callback;


}

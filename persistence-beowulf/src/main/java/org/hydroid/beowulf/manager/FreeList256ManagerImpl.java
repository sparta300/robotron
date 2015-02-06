package org.hydroid.beowulf.manager;

import org.hydroid.beowulf.overlay.FreeList256;
import org.hydroid.beowulf.overlay.FreeListRuntime;

public class FreeList256ManagerImpl implements FreeList256Manager {
	public FreeList256ManagerImpl(FreeList256 freeList) {
		runtime = freeList.getRuntime();
		fsm = new FreeSlotManagerImpl(freeList.getFreeSlotList());
	}

	@Override
	public void allocateSlot(int freeSlotIndex) {
		runtime.setFree(runtime.getFree() - 1);
		runtime.setUsed(runtime.getUsed() + 1);		
		fsm.allocateSlot(freeSlotIndex);		
	}
	
	@Override
	public void deallocateSlot(int subslotIndex) {
		runtime.setFree(runtime.getFree() + 1);
		runtime.setUsed(runtime.getUsed() - 1);
		fsm.deallocateSlot(subslotIndex);
	}
	
	@Override
	public int findFreeSlot() {
		return fsm.findFreeSlot();
	}
	
	public FreeSlotManager getFreeSlotManager() { return fsm; }
	public FreeListRuntime getFreeListRuntime() { return runtime; }
		
	private FreeListRuntime runtime;
	private FreeSlotManager fsm;	
}	

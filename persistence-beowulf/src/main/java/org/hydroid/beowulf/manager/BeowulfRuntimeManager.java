package org.hydroid.beowulf.manager;

import org.hydroid.beowulf.overlay.RepositoryOverhead;

public class BeowulfRuntimeManager implements RuntimeManager {
	public BeowulfRuntimeManager(RepositoryOverhead repositoryOverhead) {
		ro = repositoryOverhead;
	}

	public long nextBlockId() {
		long nextBlockId = ro.getNextBlockId();
		ro.setNextBlockId(nextBlockId + 1);
		return nextBlockId;
	}
	
	public long getBlockCount() {
		return ro.getNextBlockId();
	}

	private RepositoryOverhead ro;
}

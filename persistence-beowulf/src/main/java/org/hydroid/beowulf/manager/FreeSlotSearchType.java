package org.hydroid.beowulf.manager;

import static org.hydroid.beowulf.BeowulfConstants.SLOT_USED;

public enum FreeSlotSearchType {
	FREE {
		public boolean select(int pointer) {
			return pointer != SLOT_USED;
		}
	}, 
	USED {
		public boolean select(int pointer) {
			return pointer == SLOT_USED;
		}
	}, 
	TOTAL {
		public boolean select(int pointer) {
			return true;
		}
	};
		
	protected abstract boolean select(int pointer);

}

package org.hydroid.beowulf.space;

import org.hydroid.beowulf.storage.Slot;

public interface StorageListener {
	boolean offer(Slot slot);
}

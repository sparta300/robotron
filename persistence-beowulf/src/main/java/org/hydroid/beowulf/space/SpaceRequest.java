package org.hydroid.beowulf.space;

import java.util.List;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.Slot;

public interface SpaceRequest {

	int getTransactionId();

	int getRemainingSlotCount();

	boolean isSatisfied();

	void respond(Slot slot);

	List<Slot> getResponse();

	Sizing getSizing();

}

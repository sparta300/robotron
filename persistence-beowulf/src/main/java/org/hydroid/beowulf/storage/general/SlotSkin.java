package org.hydroid.beowulf.storage.general;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.general.visitor.SlotVisitor;
import org.hydroid.file.PhysicalResourceException;

public interface SlotSkin {
	void accept(SlotVisitor visitor) throws PhysicalResourceException;
	Sizing getSizing();
	SlotFinder getSlotFinder();
}

package org.hydroid.beowulf.storage.general;

import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.storage.general.visitor.SlotVisitor;
import org.hydroid.file.PhysicalResourceException;

public class SlotSkinImpl implements SlotSkin {

	public SlotSkinImpl(Sizing sizing, SlotFinder slotFinder) {
		sz = sizing;
		this.slotFinder = slotFinder;
	}

	public void accept(SlotVisitor visitor) throws PhysicalResourceException {
		visitor.visit(this);		
	}

	public Sizing getSizing() {	return sz; }
	public SlotFinder getSlotFinder() {	return slotFinder; }

	private SlotFinder slotFinder;
	private Sizing sz;
}

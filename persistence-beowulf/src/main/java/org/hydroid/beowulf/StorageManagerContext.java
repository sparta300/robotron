package org.hydroid.beowulf;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.overlay.Sizing;

public class StorageManagerContext {

	public StorageManagerContext(OverlayFactory reader, OverlayFactory forge, Sizing sizing) {
		this.reader = reader;
		this.forge = forge;
		this.sizing = sizing;
	}

	public Sizing getSizing() {	return sizing; }
	public OverlayFactory getReader() {	return reader; }
	public OverlayFactory getForge() {return forge; }

	private Sizing sizing;
	private OverlayFactory reader;
	private OverlayFactory forge;
}


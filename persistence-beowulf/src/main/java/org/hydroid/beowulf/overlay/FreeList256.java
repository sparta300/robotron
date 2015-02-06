package org.hydroid.beowulf.overlay;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import org.hydroid.beowulf.storage.LocatorFactory;

/**
 * a simple, list-based space manager that can manage up to 256 pointers, where each pointer is represented by
 * a single byte.
 * 
 * @author smiley
 */
public class FreeList256 extends CompositeOverlay {
	public FreeList256(ByteBuffer bb, OverlayFactory factory, LocatorFactory locatorFactory) {
		super(bb, overlayTypes, factory, locatorFactory);
		runtime = getComponent("flr");
		fsl = getComponent("fsl");
	}	
	
	public FreeList256(ByteBuffer bb, boolean create, OverlayFactory factory, LocatorFactory locatorFactory) {
		super(bb, overlayTypes, factory, locatorFactory);
		
		if (factory.isCreator()) {
			reset();
		}
		
		runtime = getComponent("flr");
		fsl = getComponent("fsl");		
	}
	
	public FreeListRuntime getRuntime() { return runtime; }
	public FreeSlotList getFreeSlotList() { return fsl; }
	
	private FreeListRuntime runtime;
	private FreeSlotList fsl;
	
	private static final List<String> overlayTypes = Arrays.asList("flr", "back-1", "fsl");		
}

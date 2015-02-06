package org.hydroid.beowulf.overlay;

import java.util.ArrayList;
import java.util.List;

public class OverlayTracker {
	public OverlayTracker(long homePosition) {
		home = homePosition;
	}	
	
	public void join(OverlayTracker overlay) {
		for (Object object : overlay.iterate()) {
			this.objects.add(object);
		}
	}	
	
	private Iterable<Object> iterate() {
		return objects;
	}
	
	private long home = -1L;
	private List<Object> objects = new ArrayList<Object>();
}

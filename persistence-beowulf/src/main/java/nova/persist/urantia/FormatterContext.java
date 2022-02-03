package nova.persist.urantia;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.overlay.OverlayFactory;
import org.hydroid.beowulf.storage.LocatorFactory;

public class FormatterContext {
	public ByteBuffer bb;
	public OverlayFactory overlayFactory;
	public LocatorFactory locatorFactory;
	public int blockSize;
	public int slotSize;
}

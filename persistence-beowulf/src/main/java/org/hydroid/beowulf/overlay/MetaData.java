package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.BUILD_NUMBER;
import static org.hydroid.beowulf.BeowulfConstants.MAGIC_NUMBER;
import static org.hydroid.beowulf.BeowulfConstants.MAJOR_VERSION;
import static org.hydroid.beowulf.BeowulfConstants.MINOR_VERSION;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.persist.pointer.IntPointer;

public class MetaData extends AbstractOverlay {
	private final IntPointer magic;
	private final IntPointer major;
	private final IntPointer minor;
	private final IntPointer build;

	public MetaData(ByteBuffer bb, LocatorFactory locatorFactory) {
		super(bb, locatorFactory);
		magic = new IntPointer(bb);
		major = new IntPointer(bb);
		minor = new IntPointer(bb);
		build = new IntPointer(bb);
		markEnd();
	}

	public MetaData(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset) {
		this(bb, locatorFactory);

		if (reset) {
			reset();
		}
	}

	@Override
	public void reset() {
		ByteBuffer bb = getByteBuffer();
		start();
		bb.putInt(MAGIC_NUMBER);
		bb.putInt(MAJOR_VERSION);
		bb.putInt(MINOR_VERSION);
		bb.putInt(BUILD_NUMBER);
	}

	@Override
	public String toString() {
		final StringBuilder buf = new StringBuilder();
		buf.append("magic [").append(getMagicNumberHex());
		buf.append("] version [").append(this.major.get());
		buf.append(".").append(this.minor.get());
		buf.append("] build [").append(this.build.get());
		buf.append("] key [").append(getVersionIdentifier());
		buf.append("]");
		return buf.toString();
	}

	public String getMagicNumberHex() { return "0x" + Integer.toHexString(this.magic.get()); }
	public int getMagicNumber() { return magic.get(); }
	public String getVersionIdentifier() { return "v" + major.get() + "." + minor.get() + "." + build.get(); }
	public int getMajorVersion() { return major.get(); }
	public int getMinorVersion() { return minor.get(); }
	public int getBuildNumber() { return build.get(); }
}

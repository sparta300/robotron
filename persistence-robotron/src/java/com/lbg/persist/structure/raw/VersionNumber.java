package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.AbstractStructure;

/**
 * a standard four part version number.
 * 
 * For example 4.2.1.3 where major is 4, minor is 2, build is 1 and patch is 3.
 * 
 * @author C006011
 */
public class VersionNumber extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(VersionNumber.class);

	private final ShortPointer major;
	private final ShortPointer minor;
	private final ShortPointer build;
	private final ShortPointer patch;

	public VersionNumber(ByteBuffer bb)
	{
		this(bb, false);
	}

	private VersionNumber(ByteBuffer bb, boolean reset)
	{
		super(bb);
		major = new ShortPointer(bb);
		minor = new ShortPointer(bb);
		build = new ShortPointer(bb);
		patch = new ShortPointer(bb);
		markEnd();

		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(toString());			
		}
	}
	
	public static VersionNumber forge(ByteBuffer bb)
	{
		return new VersionNumber(bb, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.putShort(SafeCast.fromIntToShort(Unset.VERSION));
		bb.putShort(SafeCast.fromIntToShort(Unset.VERSION));
		bb.putShort(SafeCast.fromIntToShort(Unset.VERSION));
		bb.putShort(SafeCast.fromIntToShort(Unset.VERSION));
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append(major.get());
		buf.append(".").append(minor.get());
		buf.append(".").append(build.get());
		buf.append(".").append(patch.get());
		return buf.toString();
	}

	public int getMajor()
	{
		return major.get();
	}

	public int getMinor()
	{
		return minor.get();
	}

	public int getBuild()
	{
		return build.get();
	}

	public int getPatch()
	{
		return patch.get();
	}
	
	public void setMajor(int major)
	{
		this.major.set(major);
	}
	
	public void setMinor(int minor)
	{
		this.minor.set(minor);
	}
	
	public void setBuild(int build)
	{
		this.build.set(build);
	}
	
	public void setPatch(int patch)
	{
		this.patch.set(patch);
	}	
}

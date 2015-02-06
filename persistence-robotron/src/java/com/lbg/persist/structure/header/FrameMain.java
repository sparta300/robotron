package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Unset;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * part of the composite structure that makes up a telemetry frame.
 * 
 * @author C006011
 */
public class FrameMain extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(FrameMain.class);

	private final Header header;
	private final LongPointer frameId;
	private final LongPointer diffNanos;

	public FrameMain(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private FrameMain(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		this.header = header;

		frameId = new LongPointer(bb);
		diffNanos = new LongPointer(bb);
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
	
	public static FrameMain forge(ByteBuffer bb, Header header)
	{
		return new FrameMain(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putLong((long)Unset.ADDRESS);
		buffer.putLong((long)Unset.DIFFERENCE);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("frameId=").append(frameId.get());
		sb.append(" diffNanos=").append(diffNanos.get());
		return sb.toString();
	}
	
	public void setFrameId(long value)
	{
		frameId.set(value);
	}	
	
	public long getFrameId()
	{
		return frameId.get();
	}
	
	public void setDiffNanos(long value)
	{
		diffNanos.set(value);
	}	
	
	public long getDiffNanos()
	{
		return diffNanos.get();
	}	
	
	public Header getHeader()
	{
		return header;
	}	
}

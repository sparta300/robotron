package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.raw.Header;

/**
 * part of the composite structure that makes up a telemetry stream.
 * 
 * @author C006011
 */
public class TelemetryStream extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(TelemetryStream.class);

	private final ShortPointer year;
	private final LongPointer startNanos;
	private final LongPointer firstFrameAddress;

	public TelemetryStream(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}

	private TelemetryStream(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);

		// can't try to set block type to an enum just yet in case we are
		// reading garbage
		year = new ShortPointer(bb);
		startNanos = new LongPointer(bb);
		firstFrameAddress = new LongPointer(bb);
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
	
	public static TelemetryStream forge(ByteBuffer bb, Header header)
	{
		return new TelemetryStream(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putShort(SafeCast.fromIntToShort(Unset.YEAR));
		buffer.putLong((long)Unset.NANO_TIME_STAMP);
		buffer.putLong((long)Unset.ADDRESS);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("year=").append(year.get());
		sb.append(" startNanos=").append(startNanos.get());
		sb.append(" firstFrame=").append(firstFrameAddress.get());
		return sb.toString();
	}
	
	public void setYear(int value)
	{
		year.set(value);
	}	
	
	public long getYear()
	{
		return year.get();
	}
	
	public void setStartNanos(long value)
	{
		startNanos.set(value);
	}	
	
	public long getStartNanos()
	{
		return startNanos.get();
	}	
	
	public void setFirstFrameAddress(long value)
	{
		firstFrameAddress.set(value);
	}	
	
	public long getFirstFrameAddress()
	{
		return firstFrameAddress.get();
	}
}

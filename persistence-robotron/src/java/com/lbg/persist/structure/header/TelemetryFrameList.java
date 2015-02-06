package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.structure.StructureReader;
import com.lbg.persist.structure.StructureType;
import com.lbg.persist.structure.raw.Header;

/**
 * a list of telemetry frames.
 * 
 * @author C006011
 */
public class TelemetryFrameList extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(TelemetryFrameList.class);

	private final TelemetryFrame[] frames;
	private final int elementCount;

	public TelemetryFrameList(ByteBuffer bb, Header header, List<StructureType> types, StructureReader reader)
	{
		this(bb, header, false);
	}
	 
	public TelemetryFrameList(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}
	
	private TelemetryFrameList(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);
		this.elementCount = header.getElementCount();
		frames = new TelemetryFrame[elementCount];
		
		for (int e = 0; e < elementCount; e++)
		{
			frames[e] = TelemetryFrame.forge(bb, header);
		}
		
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
	
	
	public static TelemetryFrameList forge(ByteBuffer bb, Header header)
	{
		return new TelemetryFrameList(bb, header, true);
	}
	
	public void reset()
	{
		moveToStart();
		
		for (TelemetryFrame f : frames)
		{
			f.reset();
		}
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("elementCount=").append(elementCount);
		
		for (int e = 0; e < elementCount; e++)
		{
			sb.append(" [").append(e).append("]=").append(getFrame(e));
		}
		
		return sb.toString();
	}

	public TelemetryFrame getFrame(int index)
	{
		return frames[index];
	}
}

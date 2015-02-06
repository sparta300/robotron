package com.lbg.persist.telemetry;

import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Swizzler;
import com.lbg.persist.daemon.ScratchBuffer;
import com.lbg.persist.daemon.ScratchBufferImpl;
import com.lbg.persist.structure.header.TimeCode;

public class FrameSinkImpl implements FrameSink
{
	private static final Logger log = LoggerFactory.getLogger(FrameSinkImpl.class);
	private final Swizzler swizzler;

	@Inject
	private FrameSinkImpl(Swizzler swizzler)
	{
		this.swizzler = swizzler;
	}

	@Override
	public void onTick(long diffNanos, Calendar now)
	{
		final ScratchBuffer scratch = new ScratchBufferImpl(32);
		final ByteBuffer bb = scratch.getByteBuffer();
		final TimeCode timeCode = TimeCode.forge(bb, null);
		timeCode.set(now);
		log.debug(timeCode.toString());
	}
}

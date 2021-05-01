package com.lbg.persist.telemetry;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.engine.service.Service;
import com.lbg.persist.engine.service.ServiceException;
import com.mfdev.utility.PropertyMap;
import com.mfdev.utility.calendar.DateTimeFactory;

/**
 * an implementation of the {@link Service} interface that implements a telemetry stream service.
 * 
 * @author C006011
 */
public class TelemetryService implements Service
{
	private static final Logger log = LoggerFactory.getLogger(TelemetryService.class);
	
	private final AtomicBoolean isStreaming = new AtomicBoolean();
	private final AtomicLong startNanosRef = new AtomicLong();
	private final DateTimeFactory clock;
	private final FrameGenerator frameGenerator;
	private final FrameListener frameListener;
    
	@Inject
	private TelemetryService(final DateTimeFactory clock, FrameGenerator frameGenerator, final FrameSink frameSink)
	{
		this.clock = clock;
		this.frameGenerator = frameGenerator;
		
		this.frameListener = new FrameListener() 
		{
			@Override
			public void onFrame(FrameGenerator source, Frame frame)
			{
				final long startNanos = startNanosRef.get();
				
				if (isStreaming.get() && startNanos != 0L)
				{
					final long nowNanos = clock.nowNanos();
					frameSink.onTick(nowNanos - startNanos, clock.nowCalendar());
				}		
			}
		};
	}
	
	@Override
	public void up(PropertyMap parameters) throws ServiceException
	{		
		startNanosRef.set(clock.nowNanos());
		log.info("starting telemetry service startNanos=" + startNanosRef.get());		
		frameGenerator.addListener(frameListener);		
		isStreaming.set(true);
	}

	@Override
	public void down()
	{		
		isStreaming.set(false);	
		frameGenerator.removeListener(frameListener);
		frameGenerator.stop();
	}
}

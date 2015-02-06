package com.lbg.persist.telemetry;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.utility.DateTimeFactory;
import com.lbg.utility.PropertyMap;

/**
 * a basic frame generator implementation.
 * 
 * @author C006011
 */
public class ExecutorBasedFrameGenerator implements FrameGenerator
{
	private static final Logger log = LoggerFactory.getLogger(ExecutorBasedFrameGenerator.class); 
	
	private static final long NO_ADDRESS = -1l;
	private final ScheduledExecutorService executor;
	private final DateTimeFactory clock;
	private final long startUtcTimeStamp;
	private final long startNanos;
	private final Set<FrameListener> listeners = new CopyOnWriteArraySet<FrameListener>();
	private final AtomicReference<Frame> currentFrame = new AtomicReference<Frame>();
	private final AtomicLong parentAddress = new AtomicLong(NO_ADDRESS);
	
	@Inject
	private ExecutorBasedFrameGenerator(PropertyMap props, DateTimeFactory clock)
	{		
		this.clock = clock;
		executor = Executors.newScheduledThreadPool(1);		
		final int period = props.getInteger("frame.generator.period");
		final String timeUnitString = props.getString("frame.generator.period.unit");
		final TimeUnit timeUnit = TimeUnit.valueOf(timeUnitString.toUpperCase());
		startUtcTimeStamp = clock.nowMillis();
		startNanos = clock.nowNanos();
		executor.scheduleAtFixedRate(getRunnable(), 0L, period, timeUnit);
	}

	
	private Runnable getRunnable()
	{
		return new Runnable() 
		{
			@Override
			public void run()
			{
				tick();				
			}			
		};
	}
		 
	@Override
	public Frame getCurrentFrame()
	{
		return currentFrame.get();		
	}
		
	private void tick()
	{
		final long now = clock.nowNanos();
		long diffNanos = now - startNanos;
		
		if (diffNanos < 0)
		{
			diffNanos = 0;
		}
		
		final Frame nextFrame = new Frame(parentAddress.get(), diffNanos, clock.nowCalendar());
		currentFrame.set(nextFrame);

		for (FrameListener listener : listeners)
		{			 
			listener.onFrame(this, nextFrame);	
		}
	}

	@Override
	public void addListener(FrameListener listener)
	{
		listeners.add(listener);		
	}
	
	@Override
	public void removeListener(FrameListener listener)
	{
		listeners.remove(listener);
	}
	
	@Override
	public long getStartTimeStamp()
	{
		return startUtcTimeStamp;
	}
	
	@Override
	public void stop()
	{
		log.info("stopping frame generator");
		executor.shutdownNow();
	}	
}

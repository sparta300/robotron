package com.lbg.persist.telemetry;

/**
 * a generator for telemetry frames.
 * 
 * @author C006011
 */
public interface FrameGenerator
{
	void addListener(FrameListener listener);

	void removeListener(FrameListener listener);

	long getStartTimeStamp();

	Frame getCurrentFrame();
	
	void stop();
}

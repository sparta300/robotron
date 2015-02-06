package com.lbg.persist.telemetry;

public interface FrameListener
{
	//void onFrame(FrameGenerator source, long diffNanos, String timeCode);

	void onFrame(FrameGenerator source, Frame frame);
}

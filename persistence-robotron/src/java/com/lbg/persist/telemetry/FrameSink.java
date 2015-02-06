package com.lbg.persist.telemetry;

import java.util.Calendar;

public interface FrameSink
{

	void onTick(long diffNanos, Calendar now);

}

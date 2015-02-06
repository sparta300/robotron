package com.lbg.persist.telemetry;

import java.util.Calendar;

public class Frame
{
	private final long diffNanos;
	private final long parentStream;
	
	public Frame(long parentStream, long diffNanos, Calendar nowCalendar)
	{
		this.parentStream = parentStream;
		this.diffNanos = diffNanos;
	
	}
}

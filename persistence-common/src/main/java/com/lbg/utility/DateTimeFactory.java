package com.lbg.utility;

import java.util.Calendar;

public interface DateTimeFactory
{
	ImmutableDate nowDate();
	Calendar nowCalendar();
	long nowMillis();
	long nowNanos();
}

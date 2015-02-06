package com.lbg.utility;

import java.util.Calendar;

public class DateTimeFactoryImpl implements DateTimeFactory
{	
	@Override
	public ImmutableDate nowDate()
	{
		final Calendar now = Calendar.getInstance();
		final int day = now.get(Calendar.DAY_OF_MONTH);
		final int month = now.get(Calendar.MONTH) + 1;
		final int year = now.get(Calendar.YEAR);
		return new ImmutableDate(day, month, year);
	}

	@Override
	public Calendar nowCalendar()
	{
		return Calendar.getInstance();
	}

	@Override
	public long nowMillis()
	{
		return System.currentTimeMillis();
	}

	@Override
	public long nowNanos()
	{		
		return System.nanoTime();
	}
}

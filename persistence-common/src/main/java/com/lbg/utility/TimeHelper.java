package com.lbg.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.mfdev.utility.Characters;

public class TimeHelper 
{
	private static final String[] timeUnits = { "nanos", "micros", "millis", "seconds" };
	private static final String ZERO_MILLIS = "time (millis): 0";
	private static final String ZERO_NANOS = "time (millis): 0";
	
	/**
	 * report the difference between two time stamps of the kind returned by {@link System#nanoTime()}.
	 * The smallest reportable difference is 1 nanosecond. 
	 */
	public static String diffNanos(long start, long now)
	{
		long diff = now - start;
		
		if (diff < 0)
		{
			return ZERO_NANOS;
		}
		
		return diffTimeUnit(0, 1, diff, TimeUnit.NANOSECONDS); 
	}
	
	/**
	 * report the difference between two time stamps of the kind returned by {@link System#currentTimeMillis()}. 
	 * The smallest reportable difference is 1 millisecond.
	 */	
	public static String diffMillis(long start, long now)
	{
		long diff = now - start;
		
		if (diff < 0)
		{
			return ZERO_MILLIS;
		}
		
		return diffTimeUnit(2, 3, diff, TimeUnit.MILLISECONDS); 
	}	

	private static String diffTimeUnit(int defaultIndex, int startIndex, long diff, TimeUnit diffTimeUnit) 
	{
		int index = startIndex;
		final int maxIndex = timeUnits.length - 1;
		String unit = timeUnits[defaultIndex];
		final long originalDiff = diff;
		
		while (diff > 1000 && index <= maxIndex)
		{
			unit = timeUnits[index++];
			diff /= 1000;
		}
		
		// return here for any difference less than a second
		if (!unit.equals(timeUnits[maxIndex]))
		{
			return "time (" + unit + "): " + diff;	
		}
		
		// otherwise switch over from decimal 10-based maths to bonkers egyptian 6-based maths
		long seconds = diff;
		long millis = TimeUnit.MILLISECONDS.convert(originalDiff, diffTimeUnit);
		
		if (millis > 1000)
		{
			millis -= (seconds * 1000);
		}
		
		if (seconds < 60)
		{
			return "time (" + unit + "): " + seconds + "." + leftPad3(millis);
		}
		
		long minutes = seconds / 60;
		seconds -= (minutes * 60);
		
		if (minutes < 60)
		{
			return "time (minutes): " + minutes + ":" + leftPad2(seconds) + "." + leftPad3(millis);
		}
		
		long hours = minutes / 60;
		minutes -= (hours * 60);
		
		return "time (hours): " + hours + ":" + leftPad2(minutes) + ":" + leftPad2(seconds) + "." + leftPad3(millis);
	}
	
	private static String leftPad2(long number)
	{
		if (number < 10)
		{
			return "0" + number;
		}
		else
		{
			return String.valueOf(number);
		}
	}
	
	private static String leftPad3(long number)
	{
		if (number < 10)
		{
			return "00" + number;
		}
		else if (number < 100)
		{
			return "0" + number;
		}
		else
		{
			return String.valueOf(number);
		}
	}

	public static String toHhMm24(Calendar calendar)
	{
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		return toHhMm24(hour, minute);
	}
	
	public static String toHhMm24(int hour, int minute)
	{
		if (hour < 0 || hour > 23)
		{
			throw new IllegalArgumentException("invalid hour: " + hour);
		}
		
		if (minute < 0 || minute > 59)
		{
			throw new IllegalArgumentException("invalid minute: " + hour);
		}
		
		return leftPad2(hour) + ":" + leftPad2(minute);
	}
	
	public static String toTimeCode(Calendar calendar)
	{
		final int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		final int hour = calendar.get(Calendar.HOUR_OF_DAY);
		final int minute = calendar.get(Calendar.MINUTE);
		final int second = calendar.get(Calendar.SECOND);		
		final int millisecond = calendar.get(Calendar.MILLISECOND);
		
		final StringBuilder sb = new StringBuilder(20);
		sb.append(leftPad3(dayOfYear)).append(Characters.COLON);
		sb.append(leftPad2(hour)).append(Characters.COLON);
		sb.append(leftPad2(minute)).append(Characters.COLON);
		sb.append(leftPad2(second)).append(Characters.DOT);
		sb.append(leftPad3(millisecond));
		
		return sb.toString();
	}
	
	public static List<Integer> toTimeCodeParts(Calendar calendar)
	{
		final List<Integer> parts = new ArrayList<Integer>();
		parts.add(calendar.get(Calendar.DAY_OF_YEAR));
		parts.add(calendar.get(Calendar.HOUR_OF_DAY));
		parts.add(calendar.get(Calendar.MINUTE));
		parts.add(calendar.get(Calendar.SECOND));		
		parts.add(calendar.get(Calendar.MILLISECOND) / 100);
		return parts;
	}

	public static String toTimeCodeString(int dayOfYear, int hour, int minute, int second, int tenth)
	{
		final StringBuilder sb = new StringBuilder(16);
		sb.append(leftPad3(dayOfYear));
		sb.append(Characters.COLON);
		sb.append(leftPad2(hour));
		sb.append(Characters.COLON);
		sb.append(leftPad2(minute));
		sb.append(Characters.COLON);
		sb.append(leftPad2(second));
		sb.append(Characters.DOT);
		sb.append(tenth);
		return sb.toString();
	}	
	
	/*
	public static ImmutableDate parseYyyyMmDd(String yyyyMmDd)
	{
		if (yyyyMmDd.length() != 8)
		{
			throw new IllegalArgumentException("date must be 8 characters long: " + yyyyMmDd);
		}
		
		final String yearString = yyyyMmDd.substring(0, 4);
		final String monthString = yyyyMmDd.substring(4, 6);
		final String dayString = yyyyMmDd.substring(6, 8);
		final int year = Integer.parseInt(yearString);
		final int month = Integer.parseInt(monthString);
		final int day = Integer.parseInt(dayString);
		
		return new ImmutableDate(day, month, year);
	}

	public static ImmutableDate parseYyyyMmDd(String yyyyMmDd, DateTimeFactory clock, String valueMeaningNow)
	{
		return parseYyyyMmDd(yyyyMmDd, clock, Arrays.asList(valueMeaningNow));
	}
	
	public static ImmutableDate parseYyyyMmDd(String yyyyMmDd, DateTimeFactory clock, List<String> valuesMeaningNow)
	{
		if (valuesMeaningNow.contains(yyyyMmDd))
		{
			return clock.nowDate();
		}
				
		if (yyyyMmDd.length() != 8)
		{
			throw new IllegalArgumentException("date must be 8 characters long: " + yyyyMmDd);
		}
		
		final String yearString = yyyyMmDd.substring(0, 4);
		final String monthString = yyyyMmDd.substring(4, 6);
		final String dayString = yyyyMmDd.substring(6, 8);
		final int year = Integer.parseInt(yearString);
		final int month = Integer.parseInt(monthString);
		final int day = Integer.parseInt(dayString);
		
		return new ImmutableDate(day, month, year);
	}
	
	public static boolean isWeekday(ImmutableDate date)
	{
		final Calendar calendar = date.toCalendar();
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		switch (dayOfWeek)
		{
		case Calendar.MONDAY:
		case Calendar.TUESDAY:
		case Calendar.WEDNESDAY:
		case Calendar.THURSDAY:
		case Calendar.FRIDAY:
				return true;
		default:
				return false;
		}
	}
	
	public static boolean isFriday(ImmutableDate date)
	{
		final Calendar calendar = date.toCalendar();
		final int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		return dayOfWeek == Calendar.FRIDAY;
	}

	public static ImmutableDate previousDay(ImmutableDate date)
	{
		final Calendar calendar = date.toCalendar();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH) + 1;
		int year = calendar.get(Calendar.YEAR);
		return new ImmutableDate(day, month, year);
	}
	*/
}

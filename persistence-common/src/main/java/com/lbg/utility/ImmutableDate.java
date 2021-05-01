package com.lbg.utility;

import java.util.Calendar;

import com.mfdev.utility.Characters;

/**
 * an immutable object representing a date, supporting the common formats 
 * used in file names etc.
 * 
 * @author C006011
 */
public class ImmutableDate implements Comparable<ImmutableDate>
{
	private static final char DEFAULT_SEPARATOR = Characters.FORWARD_SLASH;
	
	private final int day;
	private final int month;
	private final int year;
	private final int yyyymmdd;
	private final int yymmdd;
	private final int yy;
	private final String yyyymmddString;
	private final String yymmddString;
	private final String readable;

	public ImmutableDate(int day, int month, int year)
	{
		this.day = day;
		this.month = month;
		this.year = year;
		
		ensureValidDayAndMonth(day, month, year);
		
		yy = year - 2000;
		yymmdd = yy * 10000 + month * 100 + day;		
		yyyymmdd = year * 10000 + month * 100 + day; 
		
		yymmddString = String.valueOf(yymmdd);
		yyyymmddString = String.valueOf(yyyymmdd);
		
		readable = toString(DEFAULT_SEPARATOR);
	}
	
	private void ensureValidDayAndMonth(int day, int month, int year)
	{
		if (month < 1 || month > 12)
		{
			throw new IllegalArgumentException("month must be between 1 and 12");
		}

		if (day < 1 || day > 31)
		{
			throw new IllegalArgumentException("day out of range, should generally be between 1 and 31: " + day);
		}
		
		final int maxFeb = isLeapYear(year) ? 29 : 28;
		
		// according to the ryhme:
		// 30 days hath september(9), april(4), june(6) and november(11)
		// all the rest have 31
		// except february(2) alone, that has 28 clear and 29 in each leap year
		switch (day)
		{		
		case 4:
		case 6:
		case 9:
		case 11:
			if (day > 30)
			{
				throw new IllegalArgumentException("day out of range, should be between 1 and 30: " + day);
			}
			break;			
		case 2:
			if (day > maxFeb)
			{
				throw new IllegalArgumentException("day out of range, should be between 1 and " + maxFeb + ": " + day);
			}
			break;
		default:
			// already known to be between 1 and 31
			break;
		}
	}

	public ImmutableDate(int yyyymmdd)
	{
		this.yyyymmdd = yyyymmdd;
		int date = yyyymmdd;
		year = yyyymmdd / 10000;
		yy = year - 2000;
		
		date -= (10000 * year);
		month = date / 100;
		
		day = date - (100 * month);
		yymmdd = yy * 10000 + month * 100 + day;
		
		ensureValidDayAndMonth(day, month, year);
		
		yymmddString = String.valueOf(yymmdd);
		yyyymmddString = String.valueOf(yyyymmdd);
		
		readable = toString(DEFAULT_SEPARATOR);
	}
	
	/**
	 * is the year in this date a leap year.
	 * The general rule is that it is a leap year if the year is divisible by 4 (with no remainder).
	 * However there are exceptions.  If the year is also divisible by 100 it is not a leap year, 
	 * unless it is also divisible by 400 years.  So 1800 is divisible by 4 (4x450), is divisible by
	 * 100 (18x100) but is not a leap year because it is not divisible by 400.  The year 2000 is 
	 * divisible by 4 (4x500) and divisible by 100 (100x20) but is a leap year because it is also 
	 * divisible by 400 (400x5).
	 * This might not bear much resemblance to the implementation but essentially I have made it
	 * more efficient by replacing modulo operations with bitwise ANDs.  So the modulo 4 test is
	 * replaced by <tt>year & 3</tt> and the modulo 400 test is replaced by <tt>year & 15</tt>.
	 * The modulo 100 test cannot really be replaced this way but is reduced to a modulo 25
	 * because the <tt>year & 3</tt> already eliminates them.  In other words the factors of
	 * 100 can be listed as 4x5x5, if you remove the 4, you are left with 5x5=25.
	 * 
	 * @return true if the year is a leap year containing 366 days with the extra day February 29th.
	 */
	public boolean isLeapYear()
	{
		return isLeapYear(year);
	}
	
	public boolean isFriday()
	{
		final Calendar cal = toCalendar();
		final int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.FRIDAY;
	}
	
	public static boolean isLeapYear(int y)
	{
		if ((y & 3) == 0 && ((y % 25) != 0 || (y & 15) == 0))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * @return  a date integer in YYYYMMDD format.  For example, 19991225 for christmas day 1999.
	 */	
	public int toYyyymmdd()
	{
		return yyyymmdd;
	}
		
	
	@Override
	public int hashCode()
	{
		return yyyymmdd;
	}	

	@Override
	public int compareTo(ImmutableDate other)
	{
		if (other == null)
		{
			throw new IllegalArgumentException("date cannot be null");
		}
		
		// the contract for compareTo() is
		// 0 for equal
		// negative integer for less than
		// positive integer for greater than
		// NOTE: for reverse order we have swapped them around
		if (other.yyyymmdd < yyyymmdd)
		{
			return 1;
		}
		else if (other.yyyymmdd > yyyymmdd)
		{
			return -1;
		}
		
		return 0;	
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null)
		{
			return false;
		}
		
		if (getClass() != obj.getClass())
		{
			return false;
		}
		
		final ImmutableDate other = (ImmutableDate) obj;
		
		if (yyyymmdd != other.yyyymmdd)
		{
			return false;
		}
		
		return true;
	}

	/**
	 * @return  a date string in YYYYMMDD format.  For example, 19991225 for christmas day 1999.
	 */	
	public String toYyyymmddString()
	{
		return yyyymmddString;
	}	
	
	/**
	 * @return  a date integer in YYMMDD format.  For example, 991225 for christmas day 1999.
	 */
	public int toYymmdd()
	{
		return yymmdd;
	}
	
	/**
	 * @return  a date string in YYMMDD format.  For example, 991225 for christmas day 1999.
	 */
	public String toYymmddString()
	{
		return yymmddString;
	}	
	
	public int getYear()
	{
		return year;
	}
	
	public int getMonth()
	{
		return month;
	}
	
	public int getDay()
	{
		return day;
	}
	
	public String toReverseString()
	{
		return toReverseString(DEFAULT_SEPARATOR);
	}
	
	public String toReverseString(char separatorChar)
	{
		final StringBuilder sb = new StringBuilder();
		sb.append(year);		
		sb.append(separatorChar);
		appendMonthNumber(sb);
		sb.append(separatorChar);
		appendDayNumber(sb);
		return sb.toString();
	}
	
	public String toString()
	{
		return readable;
	}
	
	public String toString(char separatorChar)
	{
		// build up a readable date string
		final StringBuilder sb = new StringBuilder();
		appendDayNumber(sb);		
		sb.append(separatorChar);
		appendMonthNumber(sb);		
		sb.append(separatorChar);
		sb.append(year);
		return sb.toString();
	}
	
	private void appendDayNumber(StringBuilder sb)
	{
		if (day < 10)
		{
			sb.append('0').append(day);
		}
		else
		{
			sb.append(day);
		}
	}
	
	private void appendMonthNumber(StringBuilder sb)
	{
		if (month < 10)
		{
			sb.append('0').append(month);
		}
		else
		{
			sb.append(month);
		}
	}	
	
	private void appendMonthName(StringBuilder sb)
	{
		appendMonthName(month, sb);
	}
	
	private void appendMonthNameCamel(StringBuilder sb)
	{
		appendMonthNameCamel(month, sb);
	}
	
	public static void appendMonthName(int month, StringBuilder sb)
	{
		switch(month)
		{
		case 1: sb.append("JAN"); break;
		case 2: sb.append("FEB"); break;
		case 3: sb.append("MAR"); break;
		case 4: sb.append("APR"); break;
		case 5: sb.append("MAY"); break;
		case 6: sb.append("JUN"); break;
		case 7: sb.append("JUL"); break;
		case 8: sb.append("AUG"); break;
		case 9: sb.append("SEP"); break;
		case 10: sb.append("OCT"); break;
		case 11: sb.append("NOV"); break;
		case 12: sb.append("DEC"); break;
		default: throw new RuntimeException("invalid month: " + month);
		}	
	}
	
	public static void appendMonthNameCamel(int month, StringBuilder sb)
	{
		switch(month)
		{
		case 1: sb.append("Jan"); break;
		case 2: sb.append("Feb"); break;
		case 3: sb.append("Mar"); break;
		case 4: sb.append("Apr"); break;
		case 5: sb.append("May"); break;
		case 6: sb.append("Jun"); break;
		case 7: sb.append("Jul"); break;
		case 8: sb.append("Aug"); break;
		case 9: sb.append("Sep"); break;
		case 10: sb.append("Oct"); break;
		case 11: sb.append("Nov"); break;
		case 12: sb.append("Dec"); break;
		default: throw new RuntimeException("invalid month: " + month);
		}	
	}	
		
	public Calendar toCalendar()
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}

	/**
	 * print a date with the day as a number but the month as a three letter name. All 4 digits
	 * of the year are used.  For example, 25/DEC/1999 for christmas day 1999.
	 * Only used in the legacy DMO reports.
	 * 
	 * @param separatorChar any separator character but slash (/) and (-) are the most commonly used.
	 * @return the date string
	 */
	public String toDdMmmYyyyString(char separatorChar)
	{
		// build up a readable date string
		final StringBuilder sb = new StringBuilder();
		appendDayNumber(sb);		
		sb.append(separatorChar);
		appendMonthName(sb);		
		sb.append(separatorChar);
		sb.append(year);
		return sb.toString();
	}
	
	/**
	 * print a date with the day and month as numbers but only using the last two digits
	 * of the year such as 25/12/99 for christmas day 1999.
	 * 
	 * @param separatorChar any separator character but slash (/) and (-) are the most commonly used.
	 * @return the date string
	 */
	public String toDdMmYyString(char separatorChar)
	{
		// build up a readable date string
		final StringBuilder sb = new StringBuilder();
		appendDayNumber(sb);		
		sb.append(separatorChar);
		appendMonthNumber(sb);		
		sb.append(separatorChar);
		sb.append(yy);
		return sb.toString();
	}
	
	/**
	 * print a date with the day and month as numbers but only using the last two digits
	 * of the year such as 25/Dec/99 for christmas day 1999.
	 * 
	 * @param separatorChar any separator character but slash (/) and (-) are the most commonly used.
	 * @return the date string
	 */
	public String toDdMmmYyString(char separatorChar)
	{
		// build up a readable date string
		final StringBuilder sb = new StringBuilder();
		appendDayNumber(sb);		
		sb.append(separatorChar);
		appendMonthNameCamel(sb);		
		sb.append(separatorChar);
		sb.append(yy);
		return sb.toString();
	}
	
	/**
	 * print a date with the day and month as numbers together with all four digits
	 * of the year such as 25/12/1999 for christmas day 1999.
	 * 
	 * @param separatorChar any separator character but slash (/) and (-) are the most commonly used.
	 * @return the date string
	 */
	public String toDdMmYyyyString(char separatorChar)
	{
		// build up a readable date string
		final StringBuilder sb = new StringBuilder();
		appendDayNumber(sb);		
		sb.append(separatorChar);
		appendMonthNumber(sb);		
		sb.append(separatorChar);
		sb.append(year);
		return sb.toString();
	}
}

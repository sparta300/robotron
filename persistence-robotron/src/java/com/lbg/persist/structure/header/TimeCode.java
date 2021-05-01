package com.lbg.persist.structure.header;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.BytePointer;
import com.lbg.persist.pointer.ShortPointer;
import com.lbg.persist.structure.raw.Header;
import com.mfdev.utility.calendar.TimeHelper;

/**
 * a telemetry time code inspired by the IRIG J-2 standard.
 * 
 * The format is <code>DDD:HH:MM:SS.T</code> where DDD is day of the year and the rest is hours, minutes and second.  
 * The last part (T) is for the tenth of a second.  
 * The day of the year is between 1 and 366, to allow for leap years.  
 * The hours and minutes are in 24 hour format.
 * Since we are not using this as a wire format, we don't bother with the leading start of header SOH and the 
 * trailing carriage return and line feed CR LF defined by the J-2 standard.
 * 
 * @author C006011
 */
public class TimeCode extends AbstractStructureWithHeader
{
	private static final Logger log = LoggerFactory.getLogger(TimeCode.class);

	private final ShortPointer dayOfYear;
	private final BytePointer hourLeft;
	private final BytePointer hourRight;
	private final BytePointer minuteLeft;
	private final BytePointer minuteRight;
	private final BytePointer secondLeft;
	private final BytePointer secondRight;
	private final BytePointer tenth;

	public TimeCode(ByteBuffer bb, Header header)
	{
		this(bb, header, false);
	}	
	
	private TimeCode(ByteBuffer bb, Header header, boolean reset)
	{
		super(bb, header);

		// can't try to set block type to an enum just yet in case we are
		// reading garbage
		dayOfYear = new ShortPointer(bb);
		hourLeft = new BytePointer(bb);
		hourRight = new BytePointer(bb);
		minuteLeft = new BytePointer(bb);
		minuteRight = new BytePointer(bb);
		secondLeft = new BytePointer(bb);
		secondRight = new BytePointer(bb);
		tenth = new BytePointer(bb);
		markEnd();

		if (reset)
		{
			reset();
		}
		else
		{
			log.debug(toString());	
		}		
	}
	
	public static TimeCode forge(ByteBuffer bb, Header header)
	{
		return new TimeCode(bb, header, true);
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		moveToStart();
		buffer.putShort(SafeCast.fromIntToShort(Unset.COUNT));
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // hourLeft
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // hourRight
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // minuteLeft
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // minuteRight
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // secondLeft
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // secondRight
		buffer.put(SafeCast.fromIntToByte(Unset.COUNT)); // tenth
	}

	@Override
	public String toString()
	{
		int hour = hourRight.get();
		hour += (10 * hourLeft.get());
		
		int minute = minuteRight.get();
		minute += (10 * minuteLeft.get());
		
		int second = secondRight.get();
		second += (10 * secondLeft.get());
		
		return TimeHelper.toTimeCodeString(dayOfYear.get(), hour, minute, second, tenth.get());
	}
		
	public void set(Calendar value)
	{
		final List<Integer> parts = TimeHelper.toTimeCodeParts(value);
		setDayOfYear(parts.get(0));
		setHour(parts.get(1));
		setMinute(parts.get(2));
		setSecond(parts.get(3));
		tenth.set(parts.get(4));
	}
	
	public void setDayOfYear(int value)
	{
		final short shortValue = SafeCast.fromIntToShort(value);
		
		if (shortValue < 1 || shortValue > 366)
		{
			throw new IllegalArgumentException("day of year must be between 1 and 366");
		}
		
		dayOfYear.set(shortValue);
	}	
	
	public void setHour(int hour)
	{
		final int left = hour / 10;
		final int right = hour - (10 * left);
		hourLeft.set(left);
		hourRight.set(right);
	}
	
	public void setMinute(int minute)
	{
		final int left = minute / 10;
		final int right = minute - (10 * left);
		minuteLeft.set(left);
		minuteRight.set(right);
	}	
	
	public void setSecond(int second)
	{
		final int left = second / 10;
		final int right = second - (10 * left);
		secondLeft.set(left);
		secondRight.set(right);
	}		
	
	public int getDayOfYear()
	{
		return dayOfYear.get();
	}	
}

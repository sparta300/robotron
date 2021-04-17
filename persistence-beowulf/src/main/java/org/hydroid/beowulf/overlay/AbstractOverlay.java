package org.hydroid.beowulf.overlay;

import static com.mfdev.utility.Characters.ADDRESS;
import static com.mfdev.utility.Characters.QUESTION_MARK;
import static com.mfdev.utility.Characters.SPACE;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.LongPointer;

/**
 *
 * @author C006011
 */
abstract public class AbstractOverlay implements Overlay
{
	private int size;
	private long endPosition;
	private long startPosition;
	private final ByteBuffer bb;
	private final LocatorFactory locatorFactory;

	protected AbstractOverlay(ByteBuffer bb, LocatorFactory locatorFactory)
	{
		this.bb = bb;
		this.locatorFactory = locatorFactory;
		this.startPosition = bb.position();
	}

	public long getStart()
	{
		return startPosition;
	}

	public long getEnd()
	{
		return endPosition;
	}

	protected void markEnd()
	{
		endPosition = bb.position();
		size = SafeCast.fromLongToInt(endPosition - startPosition);
	}

	public ByteBuffer getByteBuffer()
	{
		return bb;
	}

	public void start()
	{
		bb.position(SafeCast.fromLongToInt(startPosition));
	}

	public void restart()
	{
		startPosition = bb.position();
	}

	public void end()
	{
		bb.position(SafeCast.fromLongToInt(endPosition));
	}

	public int size()
	{
		return size;
	}

	public String report()
	{
		return String.format("start=%4d end=%4d size=%4d", startPosition, endPosition, size);
	}

	protected void appendDataPointer(StringBuilder sb, String name,	LongPointer pointer, boolean spaceBefore, boolean spaceAfter)
	{
		if (spaceBefore)
		{
			sb.append(SPACE);
		}

		sb.append(name).append(ADDRESS);
		long locator = pointer.get();

		if (locator == UNSET_LOCATOR)
		{
			sb.append(QUESTION_MARK);
		}
		else
		{
			sb.append(locatorFactory.make(locator).asAddress());
		}

		if (spaceAfter)
		{
			sb.append(SPACE);
		}
	}

	protected void appendPointer(StringBuilder sb, String name,
			LongPointer pointer, boolean spaceBefore, boolean spaceAfter)
	{
		if (spaceBefore)
		{
			sb.append(SPACE);
		}

		sb.append(name).append(ADDRESS);
		long locator = pointer.get();

		if (locator == UNSET_LOCATOR)
		{
			sb.append(QUESTION_MARK);
		}
		else
		{
			sb.append(locatorFactory.make(locator).asAddress());
		}

		if (spaceAfter)
		{
			sb.append(SPACE);
		}
	}
	
	final protected LocatorFactory getLocatorFactory() {
		return locatorFactory;
	}
}

package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.UNSET_LOCATOR;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.pointer.LongPointer;

public class RepositoryOverhead extends AbstractOverlay
{
	private static final Logger logger = LoggerFactory.getLogger(RepositoryOverhead.class);
	
	private final LongPointer transactionManagerLocator;
	private final LongPointer nextBlockId;
	private final LongPointer spaceManagerLocator;

	public RepositoryOverhead(ByteBuffer buffer, LocatorFactory locatorFactory)
	{
		super(buffer, locatorFactory);
		nextBlockId = new LongPointer(buffer);
		transactionManagerLocator = new LongPointer(buffer);
		spaceManagerLocator = new LongPointer(buffer);
		markEnd();
		logger.debug(toString());
	}

	public RepositoryOverhead(ByteBuffer buffer, LocatorFactory locatorFactory, boolean reset)
	{
		this(buffer, locatorFactory);

		if (reset)
		{
			reset();
		}
	}

	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		start();
		bb.putLong(1);
		bb.putLong(UNSET_LOCATOR);
		bb.putLong(UNSET_LOCATOR);
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		buf.append("nextBlockId=").append(nextBlockId.get());
		buf.append(" transactionManager=");
		long tx = transactionManagerLocator.get();
		buf.append(tx == UNSET_LOCATOR ? "?" : tx);
		buf.append(" spaceManager=");
		long space = spaceManagerLocator.get();
		buf.append(space == UNSET_LOCATOR ? "?" : space);
		return buf.toString();
	}

	public long getNextBlockId()
	{
		return nextBlockId.get();
	}

	public long getSpaceManagerLocator()
	{
		return spaceManagerLocator.get();
	}

	public long getTransactionManagerLocator()
	{
		return spaceManagerLocator.get();
	}

	public void getSpaceManagerLocator(long value)
	{
		spaceManagerLocator.set(value);
	}

	public void setTransactionManagerLocator(long value)
	{
		transactionManagerLocator.set(value);
	}

	public void setNextBlockId(long value)
	{
		nextBlockId.set(value);
	}
}

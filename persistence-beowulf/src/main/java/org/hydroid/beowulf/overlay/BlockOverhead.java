package org.hydroid.beowulf.overlay;

import static org.hydroid.beowulf.BeowulfConstants.BYTE_DEFAULT_VALUE;
import static org.hydroid.beowulf.BeowulfConstants.UNSET_BLOCK_ID;
import static org.hydroid.beowulf.BlockType.STORAGE_BLOCK;

import java.nio.ByteBuffer;

import org.hydroid.beowulf.BlockType;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.SafeCast;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.pointer.UnsignedBytePointer;

/**
 * @author smiley
 */
public class BlockOverhead extends AbstractOverlay
{
	private static final Logger log = LoggerFactory.getLogger(BlockOverhead.class);
	
	private final UnsignedBytePointer blockTypePointer;
	private final BlockType blockType;
	private final LongPointer blockId;
	private final UnsignedBytePointer total;
	private final UnsignedBytePointer used;
	private final UnsignedBytePointer free;

	
	public BlockOverhead(ByteBuffer bb, LocatorFactory locatorFactory)
	{
		super(bb, locatorFactory);
		blockTypePointer = new UnsignedBytePointer(bb);
		blockType = BlockType.fromInt(blockTypePointer.get());
		blockId = new LongPointer(bb);
		free = new UnsignedBytePointer(bb);
		used = new UnsignedBytePointer(bb);
		total = new UnsignedBytePointer(bb);
		markEnd();
		log.debug(toString());
	}

	public BlockOverhead(ByteBuffer bb, LocatorFactory locatorFactory, boolean reset)
	{
		super(bb, locatorFactory);
		// can't try to set block type to an enum just yet in case we are
		// reading garbage
		blockTypePointer = new UnsignedBytePointer(bb);
		blockId = new LongPointer(bb);
		free = new UnsignedBytePointer(bb);
		used = new UnsignedBytePointer(bb);
		total = new UnsignedBytePointer(bb);
		markEnd();

		if (reset)
		{
			reset();
		}

		// okay, now we can set block type
		start();
		blockType = BlockType.fromInt(blockTypePointer.get());
		log.debug(toString());
		end();
	}

	public void reset()
	{
		ByteBuffer buffer = getByteBuffer();
		start();
		buffer.put(SafeCast.fromIntToUnsignedByte(STORAGE_BLOCK.getBlockType()));
		buffer.putLong(UNSET_BLOCK_ID);
		
		// Design decision DD-001/2020
		buffer.put(BYTE_DEFAULT_VALUE);
		buffer.put(BYTE_DEFAULT_VALUE);
		buffer.put(BYTE_DEFAULT_VALUE);
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("type=").append(blockType.toString());
		sb.append(" <b").append(blockId.get()).append(">");
		sb.append(" *total=").append(total.get());
		sb.append(" *used=").append(used.get());
		sb.append(" *free=").append(free.get());
		return sb.toString();
	}

	public void setBlockTypePointer(int value)
	{
		blockTypePointer.set(value);
	}

	public void setBlockId(long value)
	{
		blockId.set(value);
	}

	public void setUsed(int value)
	{
		used.set(value);
	}

	public void setFree(int value)
	{
		free.set(value);
	}

	public void setTotal(int value)
	{
		total.set(value);
	}

	public int getBlockTypePointer()
	{
		return blockTypePointer.get();
	}

	public long getBlockId()
	{
		return blockId.get();
	}

	public BlockType getBlockType()
	{
		return blockType;
	}

	public int getUsed()
	{
		return used.get();
	}

	public int getFree()
	{
		return free.get();
	}

	public int getTotal()
	{
		return total.get();
	}
}

package com.lbg.persist.structure.raw;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.BlockType;
import com.lbg.persist.SafeCast;
import com.lbg.persist.Unset;
import com.lbg.persist.pointer.BytePointer;
import com.lbg.persist.pointer.LongPointer;
import com.lbg.persist.structure.AbstractStructure;

/**
 * main block information, written at the beginning of each block.
 * 
 * @author C006011
 */
public class BlockMain extends AbstractStructure
{
	private static final Logger log = LoggerFactory.getLogger(BlockMain.class);

	private final BytePointer blockType;
	private final LongPointer blockId;
	private final LongPointer checkSum;

	public BlockMain(ByteBuffer bb)
	{
		this(bb, false);
	}

	private BlockMain(ByteBuffer bb, boolean reset)
	{
		super(bb);
		blockType = new BytePointer(bb);
		blockId = new LongPointer(bb);
		checkSum = new LongPointer(bb);
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
	
	public static BlockMain forge(ByteBuffer bb)
	{
		return new BlockMain(bb, true);
	}

	@Override
	public void reset()
	{
		ByteBuffer bb = getByteBuffer();
		moveToStart();
		bb.put(SafeCast.fromIntToByte(Unset.BLOCK_TYPE));
		bb.putLong((long)Unset.IDENTIFIER);
		bb.putLong((long)Unset.CHECK_SUM);
	}

	public String toString()
	{
		final StringBuilder buf = new StringBuilder();
		final long id = blockId.get();
		buf.append("b").append(id == Unset.IDENTIFIER ? "?" : id);	
		final int type = blockType.get();
		buf.append(" type=").append(type == Unset.BLOCK_TYPE ? "?" : type);
		final long sum = checkSum.get();
		buf.append(" checkSum=").append(sum == Unset.CHECK_SUM ? "?" : sum);
		return buf.toString();
	}

	public long getBlockId()
	{
		return blockId.get();
	}
	
	public void setBlockType(BlockType blockTypeEnum)
	{
		blockType.set(SafeCast.fromIntToUnsignedByte(blockTypeEnum.getTypeId()));
	}
	
	public void setBlockType(int blockTypeId)
	{
		blockType.set(SafeCast.fromIntToByte(blockTypeId));
	}	

	public int getBlockType()
	{
		return blockType.get();
	}
	
	public BlockType getBlockTypeEnum()
	{
		return BlockType.forTypeId(getBlockType());
	}	

	public void setBlockId(long blockId)
	{
		this.blockId.set(blockId);
	} 
	
	public long getCheckSum()
	{
		return checkSum.get();
	}
	
	public void setCheckSum(long checkSum)
	{
		this.checkSum.set(checkSum);
	}	
}

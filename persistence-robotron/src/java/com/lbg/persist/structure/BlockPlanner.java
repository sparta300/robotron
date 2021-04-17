package com.lbg.persist.structure;

import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.STRING_DATA;
import static com.lbg.persist.structure.StructureType.WILDERNESS;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.daemon.ScratchBuffer;
import com.lbg.persist.main.Calculator;
import com.lbg.persist.structure.header.StringData;
import com.mfdev.utility.PropertyHashMap;
import com.mfdev.utility.PropertyMap;

/**
 * helps to plan the structure of a block.
 * A scratch buffer is used when trying to calculate the size of a structure.
 * 
 * @author C006011
 */
public class BlockPlanner
{
	private static final Logger log = LoggerFactory.getLogger(BlockPlanner.class);
	
	private final StructureLibrary library;
	private final StructureFactory factory;
	private final ByteBuffer scratch;
	private final ByteBuffer bb;
	private final PropertyHashMap emptyParams;
	private final Map<StructureType, Integer> sizeMap = new HashMap<StructureType, Integer>();
	private final Map<String, Calculator> calculatorMap = new HashMap<String, Calculator>();
	private final int blockSize;
	private final int startPosition;
	private final int headerSize;
	private final int maxWilderness;
	private int space;
		 
	public BlockPlanner(StructureLibrary library, StructureFactory factory, ScratchBuffer scratchBuffer, ByteBuffer bb, int blockSize)
	{
		this.library = library;
		this.factory = factory;
		this.scratch = scratchBuffer.getByteBuffer();
		this.bb = bb;
		this.blockSize = blockSize;
		startPosition = bb.position();
		space = blockSize - startPosition;
		
		// create an empty parameters
		final Map<String, String> map = new HashMap<String, String>();
		emptyParams = new PropertyHashMap(map);
		
		// see how much space a minimal header with no data takes up.  We need to leave
		// this much space to create a STOP header to mark the end of the block
		factory.create(HEADER, scratch);
		headerSize = scratch.position();
		maxWilderness = space - headerSize;
		log.info("maxWilderness=" + maxWilderness);
	}
	 
	private void recalculateSpace(int currentPosition)
	{
		space = (blockSize - currentPosition) - headerSize;
	}
	
	private void recalculateSpace()
	{
		recalculateSpace(bb.position());
	}	
	
	public int getSpace()
	{
		return space;
	}

	public int getSize(StructureType type, PropertyHashMap parameters) throws PersistenceException
	{
		scratch.clear();
		factory.createWithHeader(type, scratch, parameters);
		final int size = scratch.position();
		sizeMap.put(type, size);
		return size;
	}
	
	public int getSize(StructureType type, Map<String, String> parameters) throws PersistenceException
	{
		return getSize(type, new PropertyHashMap(parameters));
	}
	
	public int getSizeNoParameters(StructureType type) throws PersistenceException
	{
		return getSize(type, emptyParams);
	}	
	
	public int write(StructureType type, PropertyHashMap parameters) throws PersistenceException
	{
		// no point in calculating the size of strings, they will all be different lengths
		if (type == STRING_DATA)
		{
			return writeString(parameters.getString("string.data").get());
		}
		
		final Integer lookUp = sizeMap.get(type);
		int knownSize = -1;
		
		if (lookUp == null)
		{
			knownSize = getSize(type, parameters);
			log.debug(type.name() + " size=" + knownSize);
		}
		else
		{
			knownSize = lookUp;			
		}
		
		if (knownSize <= space)
		{
			factory.createWithHeader(type, bb, parameters);
			recalculateSpace();
			return knownSize;
		}
		
		throw new PersistenceException("no space in current block");
	}
	
	public int write(StructureType type, Map<String, String> parameters) throws PersistenceException
	{
		return write(type, new PropertyHashMap(parameters));
	}
	
	public int write(StructureType type) throws PersistenceException
	{
		return write(type, emptyParams);
	}	
	
	public Calculator getList16Calculator(StructureType managementType, StructureType elementType) throws PersistenceException
	{
		// see how much space an empty telemetry frame list takes up
		scratch.clear();			
		library.createList16(scratch, 0, managementType, elementType);
		final int listPreambleSize = scratch.position();
		
		// go back again and see how much space a single element takes up
		scratch.clear();
		library.createList16(scratch, 1, managementType, elementType);
		final int spacePerElement = scratch.position() - listPreambleSize;
		final Calculator calculator =  new Calculator(listPreambleSize, spacePerElement);
		final String key = managementType.name() + ":" + elementType.name(); 
		calculatorMap.put(key, calculator);
		return calculator;
	}
	
	public void grazeList16(int elementCount, StructureType managementType,	StructureType elementType) throws PersistenceException
	{
		final String key = managementType.name() + ":" + elementType.name();
		Calculator calculator = calculatorMap.get(key);
		
		if (calculator == null)
		{
			calculator = getList16Calculator(managementType, elementType);
		}
		
		library.createList16(bb, elementCount, managementType, elementType);
		
		recalculateSpace();
	}
	
	/**
	 * converts the remainder of the block into a wilderness area.
	 * The remaining space total takes account of the STOP header but we need another header for the wilderness structure.
	 * @throws PersistenceException 
	 */
	public void createWilderness() throws PersistenceException
	{
		final int wildernessSize = space - headerSize;
		
		if (wildernessSize < 0)
		{
			throw new PersistenceException("not enough space for a wilderness");
		}

		final Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("wilderness.space", String.valueOf(wildernessSize));
		factory.createWithHeader(WILDERNESS, bb, new PropertyHashMap(parameters));
		factory.createStopHeader(bb);
	}

	/**
	 * writes a string.
	 * 
	 * @param content
	 * @return
	 * @throws PersistenceException
	 */
	public int writeString(String content) throws PersistenceException
	{
		scratch.clear();
		
		final Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("string.data", content);
		final StringData stringData = (StringData) factory.createWithHeader(STRING_DATA, scratch, new PropertyHashMap(parameters));
		final int dataLength = stringData.getHeader().getDataSize();
				
		if (dataLength <= space)
		{
			factory.createWithHeader(STRING_DATA, bb, new PropertyHashMap(parameters));
			recalculateSpace();
			return dataLength;
		}
		 
		throw new PersistenceException("not enough space for a string");
	}
}

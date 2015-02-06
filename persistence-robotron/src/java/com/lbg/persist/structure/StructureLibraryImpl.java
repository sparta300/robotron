package com.lbg.persist.structure;

import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.COUNT_16;
import static com.lbg.persist.structure.StructureType.FRAME_SEGMENT;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.LIST_16;
import static com.lbg.persist.structure.StructureType.LIST_BOOLEAN_8;
import static com.lbg.persist.structure.StructureType.LIST_DOUBLE;
import static com.lbg.persist.structure.StructureType.LIST_INTEGER;
import static com.lbg.persist.structure.StructureType.LIST_TELEMETRY_FRAME;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.STORE_MAIN;
import static com.lbg.persist.structure.StructureType.STRING_DATA;
import static com.lbg.persist.structure.StructureType.TELEMETRY_STREAM;
import static com.lbg.persist.structure.StructureType.TIME_CODE;
import static com.lbg.persist.structure.StructureType.VERSION;
import static com.lbg.persist.structure.StructureType.WILDERNESS;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.SafeCast;
import com.lbg.persist.StringCodec;
import com.lbg.persist.Swizzler;
import com.lbg.persist.Unset;
import com.lbg.persist.structure.header.BooleanList8;
import com.lbg.persist.structure.header.Count16;
import com.lbg.persist.structure.header.DoubleList;
import com.lbg.persist.structure.header.IntegerList;
import com.lbg.persist.structure.header.List16;
import com.lbg.persist.structure.header.StringData;
import com.lbg.persist.structure.header.TelemetryFrameList;
import com.lbg.persist.structure.header.TelemetryStream;
import com.lbg.persist.structure.header.TimeCode;
import com.lbg.persist.structure.header.Wilderness;
import com.lbg.persist.structure.raw.BlockMain;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Header;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.StoreMain;
import com.lbg.persist.structure.raw.VersionNumber;
import com.lbg.utility.PropertyHashMap;
import com.lbg.utility.PropertyMap;

/**
 * an implementation of a library of known structures.
 * 
 * @author C006011
 */
public class StructureLibraryImpl implements StructureLibrary
{
	private final Logger log = LoggerFactory.getLogger(StructureLibraryImpl.class);
	
	private static final short SHORT_1 = SafeCast.fromIntToShort(1);

	private final Map<StructureType, BasicStructureCommand> basicMap = new HashMap<StructureType, BasicStructureCommand>();
	private final Map<StructureType, HeaderStructureCommand> headerMap = new HashMap<StructureType, HeaderStructureCommand>();
	
	@Inject
	private StructureLibraryImpl(final StructureReader structureReader, final StructureFactory structureFactory, final StringCodec codec)
	{
		basicMap.put(MAGIC, new BasicStructureCommand() 
		{
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new Magic(bb);
			}

			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return Magic.forge(bb);
			}		
		});
		
		basicMap.put(VERSION, new BasicStructureCommand() 
		{
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new VersionNumber(bb);
			}	
			
			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return VersionNumber.forge(bb);
			}				
		});
				
		basicMap.put(HEADER, new BasicStructureCommand() 
		{
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new Header(bb);
			}		
			
			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return Header.forge(bb);
			}				
		});	
		

		basicMap.put(GEOMETRY, new BasicStructureCommand() 
		{
			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return Geometry.forge(bb);
			}
			
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new Geometry(bb);
			}		
		});
		
		basicMap.put(BLOCK_MAIN, new BasicStructureCommand() 
		{
			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return BlockMain.forge(bb);
			}
			
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new BlockMain(bb);
			}		
		});	
		
		basicMap.put(STORE_MAIN, new BasicStructureCommand() 
		{
			@Override
			public Structure create(ByteBuffer bb, Swizzler swizzler)
			{
				return StoreMain.forge(bb);
			}
			
			@Override
			public Structure read(ByteBuffer bb, Swizzler swizzler)
			{
				return new StoreMain(bb);
			}		
		});			
				
		headerMap.put(WILDERNESS, new HeaderStructureCommand()
		{
			@Override
			public Structure read(ByteBuffer bb, Header header, Swizzler swizzler)
			{
				return new Wilderness(bb, header);
			}

			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int space = parameters.getInteger("wilderness.space");
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(WILDERNESS.getId()));
				header.setElementCount(SHORT_1);
				header.setDataSize(SafeCast.fromIntToShort(space));
				return Wilderness.forge(bb, header);
			}			
		});
		
		headerMap.put(LIST_16, new HeaderStructureCommand()
		{
			@Override
			public Structure read(ByteBuffer bb, Header header, Swizzler swizzler)
			{
				return new List16(bb, header);
			}

			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final Header header = createHeader(bb, LIST_16, 1, 0);
				return List16.forge(bb, header);
			}			
		});		
		
		headerMap.put(LIST_BOOLEAN_8, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int elementCount = parameters.getInteger("element.count");
				final Header header = Header.forge(bb);
				final int start = bb.position();
				header.setType(SafeCast.fromIntToShort(LIST_BOOLEAN_8.getId()));
				header.setElementCount(SafeCast.fromIntToShort(elementCount));
				final BooleanList8 list = BooleanList8.forge(bb, header);
				header.setDataSize(SafeCast.fromIntToShort(bb.position() - start));
				return list;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new BooleanList8(bb, header);
			}			
		});
		
		headerMap.put(LIST_DOUBLE, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int elementCount = parameters.getInteger("element.count");
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(LIST_DOUBLE.getId()));
				header.setElementCount(SafeCast.fromIntToShort(elementCount));
				final int pos = bb.position();
				final DoubleList list =  DoubleList.forge(bb, header);
				final int dataSize = bb.position() - pos;
				header.setDataSize(SafeCast.fromIntToShort(dataSize));
				return list;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new DoubleList(bb, header);
			}			
		});	
		
		headerMap.put(LIST_INTEGER, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int elementCount = parameters.getInteger("element.count");
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(LIST_INTEGER.getId()));
				header.setElementCount(SafeCast.fromIntToShort(elementCount));
				final int pos = bb.position();
				IntegerList list = IntegerList.forge(bb, header);
				final int dataSize = bb.position() - pos;
				header.setDataSize(SafeCast.fromIntToShort(dataSize));
				return list;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new IntegerList(bb, header);
			}			
		});			
		
		
		headerMap.put(TELEMETRY_STREAM, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(TELEMETRY_STREAM.getId()));
				header.setElementCount(SafeCast.fromIntToShort(1));
				final int pos = bb.position();
				final TelemetryStream stream = TelemetryStream.forge(bb, header);
				final int dataSize = bb.position() - pos;
				header.setDataSize(SafeCast.fromIntToShort(dataSize));
				return stream;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new TelemetryStream(bb, header);
			}			
		});	
		
		headerMap.put(LIST_TELEMETRY_FRAME, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int elementCount = parameters.getInteger("element.count");
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(LIST_TELEMETRY_FRAME.getId()));
				header.setElementCount(SafeCast.fromIntToShort(elementCount));
				final int startPos = bb.position();
				final TelemetryFrameList list = TelemetryFrameList.forge(bb, header);
				final int dataSize = bb.position() - startPos;
				header.setDataSize(SafeCast.fromIntToShort(dataSize));
				return list;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new TelemetryFrameList(bb, header);
			}			
		});		
		
		headerMap.put(FRAME_SEGMENT, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters) throws PersistenceException
			{
				return createList16(bb, parameters);
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new TelemetryStream(bb, header);
			}			
		});		
 
		headerMap.put(TIME_CODE, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(TIME_CODE.getId()));
				header.setElementCount(SHORT_1);
				final int pos = bb.position();
				final TimeCode timeCode = TimeCode.forge(bb, header);
				header.setDataSize(SafeCast.fromIntToShort(bb.position() - pos));
				return timeCode;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler)
			{
				return new TimeCode(bb, header);
			}			
		});		

		headerMap.put(STRING_DATA, new HeaderStructureCommand()
		{
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters) throws PersistenceException
			{
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(STRING_DATA.getId()));
				header.setElementCount(SHORT_1);
				final int pos = bb.position();
				final String data = parameters.getString("string.data");
				bb.put(codec.encode(data));
				final int length = bb.position() - pos;
				header.setDataSize(SafeCast.fromIntToShort(length));
				return StringData.forge(bb, header, codec);
			}

			@Override
			public Structure read(ByteBuffer bb, Header header,	Swizzler swizzler) throws PersistenceException
			{
				return new StringData(bb, header, codec);
			}			
		});			

		headerMap.put(COUNT_16, new HeaderStructureCommand()
		{						
			@Override
			public Structure createWithHeader(ByteBuffer bb, PropertyMap parameters)
			{
				final int elementCount = parameters.getInteger("element.count");
				final Header header = Header.forge(bb);
				header.setType(SafeCast.fromIntToShort(COUNT_16.getId()));
				header.setElementCount(SHORT_1);
				final int pos = bb.position();
				final Count16 count = Count16.forge(bb, header);
				count.setCount(SafeCast.fromIntToShort(elementCount));
				header.setDataSize(SafeCast.fromIntToShort(bb.position() - pos));
				return count;
			}

			@Override
			public Structure read(ByteBuffer bb, Header header, Swizzler swizzler)
			{
				return new Count16(bb, header);
			}
		});

		log.info("structure library created");
	}
	
	@Override
	public Structure createList16(ByteBuffer bb, PropertyMap parameters) throws PersistenceException
	{
		final int elementCount = parameters.getInteger("element.count");
		final StructureType managementType = StructureType.valueOf(parameters.getString("management.type"));
		final StructureType elementType = StructureType.valueOf(parameters.getString("element.type"));
		return createList16(bb, elementCount, managementType, elementType, parameters);
	}

	@Override
	public Structure createList16(ByteBuffer bb, int elementCount, StructureType managementType, StructureType elementType) throws PersistenceException
	{
		final Map<String, String> map = new HashMap<String, String>();
		map.put("element.count", String.valueOf(elementCount));
		map.put("element.type", elementType.name());
		map.put("management.type", managementType.name());
		return createList16(bb, new PropertyHashMap(map));
	}
	
	@Override
	public Structure createList16(ByteBuffer bb, int elementCount, StructureType managementType, 
                                  StructureType elementType, PropertyMap parameters) throws PersistenceException
	{
		// write the header to start a new composite
		final Header header = createHeaderSizeUnset(bb, LIST_16, 1);
		final int pos = bb.position();
		
		// write the main type
		final Count16 count = Count16.forge(bb, header);
						
		// write n x management type		
		final HeaderStructureCommand managementCommand = headerMap.get(managementType);
		final Structure mangementList = managementCommand.createWithHeader(bb, parameters);
		
		// write n x element type		
		final HeaderStructureCommand elementCommand = headerMap.get(elementType);
		final Structure elementList = elementCommand.createWithHeader(bb, parameters);
						
		// write the data size
		header.setDataSize(SafeCast.fromIntToShort(bb.position() - pos));
		bb.position(pos);
		return new List16(bb, header);	
	}
	
	private Header createHeaderSizeUnset(ByteBuffer bb, StructureType type, int elementCount)
	{
		final Header header = Header.forge(bb);
		header.setType(SafeCast.fromIntToShort(type.getId()));
		header.setElementCount(SHORT_1);
		header.setDataSize(SafeCast.fromIntToShort(Unset.SIZE));
		return header;
	}
 
	private Header createHeader(ByteBuffer bb, StructureType type, int elementCount, int dataSize)
	{
		final Header header = Header.forge(bb);
		header.setType(SafeCast.fromIntToShort(type.getId()));
		header.setElementCount(SHORT_1);
		header.setDataSize(SafeCast.fromIntToShort(dataSize));
		return header;
	}
	
	@Override
	public HeaderStructureCommand getHeaderCommand(StructureType type)
	{
		return headerMap.get(type);
	}

	@Override
	public BasicStructureCommand getBasicCommand(StructureType type)
	{
		return basicMap.get(type);
	}
}

package com.lbg.persist.structure;

import static com.lbg.persist.structure.StructureType.FINISH_COMPOSITE;
import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.START_COMPOSITE;
import static com.lbg.persist.structure.StructureType.STOP;

import java.nio.ByteBuffer;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.PersistenceException;
import com.lbg.persist.structure.raw.Header;

/**
 * an implementation of a block reader.
 * A block is considered to be a sequence of components.  Most block components have a fixed size header that
 * tells you type of component it is and how much data should be read.  So the  basic pattern is to read the
 * header and then, depending upon the type, read the following structure.  Some headers are just markers and
 * have no following data, such as STOP, START_COMPOSITE and FINISH_COMPOSITE.  These will have a dataSize of
 * zero and are used to control the behaviour of the block reader.  The default behaviour is to keep reading
 * headers until you find a STOP header.  If the structure type is unknown to the engine, then at least you 
 * can skip the entire structure because the header tells you how long it is.  
 * 
 * The exception to the basic read-until-stop pattern is
 * when you have a structure that is in fact just a sequence of smaller structures, otherwise known as a
 * composite structure.  There is nothing magic about composite structures, it basically means that we want to
 * consider it as one component rather than each of the constituent parts.  So when we read a START_COMPOSITE
 * header, we collect together all the components we encounter until we read a FINISH_COMPOSITE header.  The
 * significance of the composite comes when you start addressing components.  So the first component in the
 * block is component 0 and the next component is component 1 and the next is component 2 and so on, one  
 * component per header encountered, as long as it isn't a marker header.  So, if there are 20
 * components in the block and none of them are composite components, the valid component IDs would be
 * 0 through to 19.  However, if the second component was in fact a composite component with 18 constituent
 * parts then the valid component IDs would be 0, 1 and 2 only.  Component 0 and component 2 would both address
 * only a single component.  However, component 1 would address the entire 18-part composite component. 
 *  
 * @author C006011
 */
public class BlockReaderImpl implements BlockReader
{
	private static final Logger log = LoggerFactory.getLogger(BlockReaderImpl.class);
	
	private static final int UNSET = -1;
	private final StructureReader reader;
	private final Set<StructureType> supportedStructures;
	
	@Inject
	public BlockReaderImpl(StructureReader reader, Set<StructureType> supportedStructures)
	{
		this.reader = reader;
		this.supportedStructures = supportedStructures;
	}

	@Override
	public void readStructures(ByteBuffer bb, BlockBuilder builder) throws PersistenceException
	{
		// remaining components can be anything but all have a header section.
		// Spin through them all until you get the STOP header
		Header header = null;
		int type = -1;
		int size = -1;
		CompositeStructureBuilder compositeBuilder = null;
		boolean createComposite = false;
		int compositeHeaderStart = UNSET;
		Header compositeHeader = null;
		
		do
		{			
			final int headerStart = bb.position();
			header = (Header) reader.readStructure(HEADER, bb);		
			compositeHeader = header;
			type = header.getType();
			size = header.getDataSize();
			final int dataStart = bb.position();
			final StructureType structureEnum = StructureType.forId(type);
			log.debug("<<" + structureEnum.name()  + ">> headerStart=" + headerStart + " dataStart=" + dataStart);
				
			// only add supported structures to the builder
			if (structureEnum != null && supportedStructures.contains(structureEnum))
			{				
				if (type != STOP.getId())
				{
					final Structure structure = reader.readStructure(structureEnum, header, bb);
					
					if (createComposite)
					{						
						compositeBuilder = new CompositeStructureBuilder(bb, structureEnum, compositeHeaderStart, compositeHeader);
						createComposite = false;
						compositeHeaderStart = UNSET;
						compositeHeader = null;
					}
					else if (compositeBuilder != null)
					{
						compositeBuilder.addComponent(structureEnum, structure);
					}
					else
					{
						builder.addComponent(structureEnum, structure);
					}
				}
			}
			else if (type == START_COMPOSITE.getId())
			{
				createComposite = true;
				compositeHeaderStart = headerStart;
			}
			else if (type == FINISH_COMPOSITE.getId())
			{				
				builder.addCompositeComponent(compositeBuilder);
				compositeBuilder = null;
			}
			else
			{
				// skip past the data
				bb.position(dataStart + size);
			}
		} while (type != STOP.getId());	
	}
}

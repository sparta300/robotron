package com.lbg.persist.engine;

import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.STORE_MAIN;
import static com.lbg.persist.structure.StructureType.VERSION;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.Adler32;

import javax.inject.Inject;

import org.hydroid.file.PhysicalResourceException;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Address;
import com.lbg.persist.NioHelper;
import com.lbg.persist.PersistConstants;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.Swizzler;
import com.lbg.persist.Unset;
import com.lbg.persist.structure.Block;
import com.lbg.persist.structure.BlockBuilder;
import com.lbg.persist.structure.BlockReader;
import com.lbg.persist.structure.Structure;
import com.lbg.persist.structure.StructureReader;
import com.lbg.persist.structure.raw.BlockMain;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.StoreMain;
import com.lbg.persist.structure.raw.VersionNumber;
import com.mfdev.utility.PropertyMap;
import com.mfdev.utility.SafeCast;

/**
 * an basic implementation of {@link TranslationLookasideBuffer}.
 * 
 * @author C006011
 */
public class TranslationLookasideBufferImpl implements TranslationLookasideBuffer
{
	private static final Logger log = LoggerFactory.getLogger(TranslationLookasideBufferImpl.class);
	
	private final PageDaemon pageDaemon;
	private final Swizzler swizzler;
	private final BlockReader blockReader;
	private final StructureReader structureReader;
	
	private final ConcurrentMap<Integer, Block> blockMap = new ConcurrentHashMap<Integer, Block>();
	private final ConcurrentMap<String, Long> namedComponents = new ConcurrentHashMap<String, Long>();
	private final ConcurrentMap<String, Structure> rootComponents = new ConcurrentHashMap<String, Structure>();
	private final ConcurrentMap<Long, Structure> components = new ConcurrentHashMap<Long, Structure>();
		
	private final AtomicInteger lookasideId = new AtomicInteger(1);
	private final ExecutorService executor;
	
	@Inject
	private TranslationLookasideBufferImpl(PageDaemon pageDaemon, Swizzler swizzler, PropertyMap props, 
                                           BlockReader blockReader, StructureReader structureReader)
	{
		this.pageDaemon = pageDaemon;
		this.swizzler = swizzler;
		this.blockReader = blockReader;
		this.structureReader = structureReader;
		executor = Executors.newFixedThreadPool(props.getInteger("tlb.pool.size").get());
	}

	@Override
	public void registerName(String name, Structure structure)
	{
		final Structure replacedStructure = rootComponents.putIfAbsent(name, structure);
		
		if (replacedStructure != null)
		{
			log.debug("root component registered: " + name);
		}
	}
	
	@Override
	public void registerName(String name, int blockId, int componentId)
	{
		final Address address = swizzler.make(blockId, componentId);
		final Long addressLong = namedComponents.putIfAbsent(name, address.asLong());
		
		if (addressLong != null)
		{
			log.debug("named component registered: " + name + "->" + addressLong);
		}
	}
	
	@Override
	public void registerName(String name, long address)
	{
		registerName(name, swizzler.make(address));
	}	

	@Override
	public void registerName(String name, Address address)
	{
		registerName(name, SafeCast.fromLongToInt(address.getBlockId()), address.getStructureId());
	}

	@Override
	public int forComponent(RepositoryFileHolder holder, LookasideTask task, AccessMode mode, String name) throws PhysicalResourceException, PersistenceException
	{
		final Long addressLong = namedComponents.get(name);
		
		// if not registered
		if (addressLong == null)
		{
			final int id = lookasideId.getAndIncrement();
			executor.submit(new LookasideTaskHolder(mode, id, task, name));
			return id;
		}
		
		final Address address = swizzler.make(addressLong);
		return forComponent(holder, task, mode, SafeCast.fromLongToInt(address.getBlockId()), address.getStructureId());
	}

	@Override
	public int forComponent(RepositoryFileHolder holder, LookasideTask task, AccessMode mode, int blockId, int componentId) throws PhysicalResourceException, PersistenceException
	{
		final Address address = swizzler.make(blockId, componentId);
		final Structure structureLookUp = components.get(address.asLong());
		final int id = lookasideId.getAndIncrement();

		// cache hit
		if (structureLookUp != null)
		{
			executor.submit(new LookasideTaskHolder(mode, id, task, structureLookUp, address));
			return id;
		}	
				
		// cache miss
		log.debug("loading component b" + blockId + "s" + componentId);
		final PageIdentifier pageId = new PageIdentifier(holder.getFile(), blockId * 1L, holder.getBlockSize());
		final Page page = pageDaemon.fetch(pageId);
		final ByteBuffer pageByteBuffer = page.getByteBuffer();
		
		Block block = null;
		
		if (blockId == 0)
		{
			block = readRootBlock(holder, pageByteBuffer); 
		}
		else
		{
			block = readBlock(blockId, pageByteBuffer);
		}
		
		blockMap.put(blockId, block);
		final Structure structure = block.getComponent(componentId);
		
		// structure not found
		if (structure == null)
		{
			executor.submit(new LookasideTaskHolder(mode, id, task, address));
		}
		else
		{
			executor.submit(new LookasideTaskHolder(mode, id, task, structure, address));
		}
		
		return id;
	}	
	
	@Override
	public Block readRootBlock(RepositoryFileHolder holder, final ByteBuffer bb) throws PersistenceException
	{
		// component [0]
		final Magic magic = (Magic) structureReader.readStructure(MAGIC, bb);		
		assert magic.getMagic() == PersistConstants.MAGIC;
		
		// component [1]
		final BlockMain blockMain = (BlockMain) structureReader.readStructure(BLOCK_MAIN, bb);
		assert blockMain.getBlockId() == 0;
		registerName(RegisteredNames.BLOCK_MAIN.getKey(), blockMain);
		
		final long expectedCheckSum = blockMain.getCheckSum();
		
		if (expectedCheckSum != Unset.CHECK_SUM)
		{				
			final Adler32 checkSum = new Adler32();
			checkSum.update(bb.array());
			assert checkSum.getValue() == expectedCheckSum;		
		}
		
		// component [2]
		final VersionNumber version = (VersionNumber) structureReader.readStructure(VERSION, bb);
		registerName(RegisteredNames.VERSION_NUMBER.getKey(), version);
		
		// component [3]
		final Geometry geometry = (Geometry) structureReader.readStructure(GEOMETRY, bb);
		registerName(RegisteredNames.GEOMETRY.getKey(), geometry);
		
		// component [4]
		final StoreMain storeMain = (StoreMain) structureReader.readStructure(STORE_MAIN, bb);
		registerName(RegisteredNames.STORE_MAIN.getKey(), storeMain);
		
		// register the manifest itself too
		final Address manifestAddress = swizzler.make(storeMain.getManifestAddress());
		registerName(RegisteredNames.MANIFEST.getKey(), SafeCast.fromLongToInt(manifestAddress.getBlockId()), manifestAddress.getStructureId());				
		
		log.info("file version: " + version.toString());
		log.info("blockSize=" + geometry.getBlockSize());
		log.info("maxBlockCount=" + geometry.getMaxBlockCount());		
		log.info("manifest=b" + manifestAddress.getBlockId() + "s" + manifestAddress.getStructureId());
		
		log.info("reading the rest of the root block " + NioHelper.toString(bb));
		final BlockBuilder builder = new BlockBuilder();
		blockReader.readStructures(bb, builder);			
		return builder.build();
	}
	
	@Override
	public Block readBlock(int blockId, ByteBuffer bb) throws PersistenceException
	{
		log.debug("reading block b" + blockId);
		final BlockMain blockMain = (BlockMain) structureReader.readStructure(BLOCK_MAIN, bb);
		assert blockMain.getBlockId() == blockId;
				
		log.info("reading the rest of the non-root block b" + blockId + " " + NioHelper.toString(bb));
		final BlockBuilder builder = new BlockBuilder();
		blockReader.readStructures(bb, builder);
		return builder.build();
	}	
}

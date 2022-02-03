package com.lbg.persist.engine;

import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.VERSION;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.Page;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Address;
import com.lbg.persist.PersistConstants;
import com.lbg.persist.PersistenceException;
import com.lbg.persist.engine.service.Service;
import com.lbg.persist.engine.service.ServiceException;
import com.lbg.persist.engine.service.ServiceRegistry;
import com.lbg.persist.structure.Block;
import com.lbg.persist.structure.Structure;
import com.lbg.persist.structure.StructureReader;
import com.lbg.persist.structure.header.StringData;
import com.lbg.persist.structure.raw.BlockMain;
import com.lbg.persist.structure.raw.Geometry;
import com.lbg.persist.structure.raw.Magic;
import com.lbg.persist.structure.raw.VersionNumber;
import com.mfdev.utility.PropertyMap;
import com.mfdev.utility.json.JsonArray;
import com.mfdev.utility.json.JsonObject;
import com.mfdev.utility.json.JsonValue;

/**
 * an implementation of a engine, which preloads and pins the root block.
 * The reader expects three fixed components: the magic, the version and the geometry of the file.
 * After that the components can be anything and can come in any order but each one must be preceeded 
 * by a header block giving the type and the length of the data, so that unknown block types can be
 * skipped.
 * 
 * @author C006011
 */
public class RobotronEngine implements Engine
{
	private static final Logger log = LoggerFactory.getLogger(RobotronEngine.class);
	
	private static final long ROOT_BLOCK_ID = PersistConstants.ROOT_BLOCK_ID;
	
	private final StructureReader reader;
	private final PageDaemon pageDaemon;
	private final int peekSize;
	private final Provider<TranslationLookasideBuffer> tlbProvider;
	private final ServiceRegistry serviceRegistry;
	private final PropertyMap properties;
	private final CopyOnWriteArrayList<String> runningServices = new CopyOnWriteArrayList<String>();
	
	@Inject
	private RobotronEngine(StructureReader reader, PageDaemon pageDaemon, ServiceRegistry serviceRegistry, 
                           @Named("telemetry") Service telemetryService,
                           PropertyMap properties, Provider<TranslationLookasideBuffer> tlbProvider)
	{
		this.reader = reader;
		this.pageDaemon = pageDaemon;
		this.serviceRegistry = serviceRegistry;
		this.peekSize = properties.getInteger("file.peek.size").get();
		this.properties = properties;
		this.tlbProvider = tlbProvider;
		
		serviceRegistry.register("telemetry-service", telemetryService);
	}
		
	@Override
	public void up()
	{
		log.info("bringing up the engine ...");
		
		
	}



	/**
	 * load the root page which is block zero.
	 * The root page always stays pinned in the cache because it should contain the data that is important or likely to change.
	 */
	private Page loadRootPage(RepositoryFileHolder fileHolder) throws PhysicalResourceException, PersistenceException, EngineException
	{
		ByteBuffer bb = null;
		final RepositoryFile file = fileHolder.getFile();
		
		try
		{
			bb = file.read(peekSize);
		}
		catch (IOException ioe)
		{
			throw new EngineException("could not peek file");
		}
		
		final Magic magic = (Magic) reader.readStructure(MAGIC, bb);
		
		if (magic.getMagic() != PersistConstants.MAGIC)
		{
			throw new EngineException("invalid magic number in file");
		}
		
		final BlockMain blockMain = (BlockMain) reader.readStructure(BLOCK_MAIN, bb);
		assert blockMain.getBlockId() == ROOT_BLOCK_ID;
		
		final VersionNumber version = (VersionNumber) reader.readStructure(VERSION, bb);	
		assertCompatibility(version);
		
		// the geometry is need for the page daemon and the TLB, so set it in the
		// holder now to make it available.
		final Geometry geometry = (Geometry) reader.readStructure(GEOMETRY, bb);
		fileHolder.setGeometry(geometry);
		final int blockSize = geometry.getBlockSize();
		
		// load in and pin the first (root) page
		final PageIdentifier page0ID = PageIdentifier.forBlock(file, ROOT_BLOCK_ID, blockSize);
		final Page page0  = pageDaemon.pageIn(page0ID);
		pageDaemon.pin(page0);

		return page0;
	}
	
	private void assertCompatibility(VersionNumber version)
	{
		// assume it is compatible for now
	}

	public void open(final RepositoryFile file) throws PhysicalResourceException, PersistenceException, EngineException
	{
		// read the root block.  Generally this is where you will find everything you need to get going.
		final TranslationLookasideBuffer tlb = tlbProvider.get();
		final RepositoryFileHolder holder = new RepositoryFileHolder(file, tlb);
		final Page rootPage = loadRootPage(holder);
		final ByteBuffer bb = rootPage.getByteBuffer();
		final Block rootBlock = tlb.readRootBlock(holder, bb);
		
		final LookasideTask loadManifestTask = new LookasideTask()
		{
			
			@Override
			public void onNotFound(int lookasideId, String name, Address address)
			{
				if (name != null)
				{
					log.error("could not find named component " + name + " for repository file " + holder.getFile().toString());
				}
				else
				{
					final String description = "b" + address.getBlockId() + "s" + address.getStructureId();
					log.error("could not find component " + description + " for repository file " + holder.getFile().toString());
				}
			}
			
			@Override
			public void execute(int lookasideId, Structure component)
			{												
				if (!(component instanceof StringData))
				{
					log.error("manifest address does not point to string data");
				}
				
				final StringData manifestData = (StringData) component;
				
				try
				{
					readManifest(holder, manifestData.toString());
				}
				catch (Exception e)
				{
					log.error("failed to process manifest", e);
				}
			}
		};
		
		// submit the task to load the manifest
		final int id = tlb.forComponent(holder, loadManifestTask, AccessMode.READ_ONLY, RegisteredNames.MANIFEST.getKey());
		
		
		
		
	}
	
	private void readManifest(RepositoryFileHolder holder, String manifestString) throws PhysicalResourceException, PersistenceException
	{
		log.debug("about to read the manifest ...");
		log.debug(manifestString);
		
		final JsonObject manifest = JsonObject.readFrom(manifestString);
		final String contentType = manifest.get("content-type").asString();
		
		loadDataStructures(holder, manifest.get("structures").asArray());
		 
		try
		{
			startServices(contentType, manifest.get("services").asArray());
		}
		catch (ServiceException e)
		{
			throw new PersistenceException("failed to start all of the required services", e);
		}
	}
	
	private void loadDataStructures(RepositoryFileHolder holder, JsonArray structures) throws PhysicalResourceException, PersistenceException
	{
		for (JsonValue value : structures)
		{
			final JsonObject dataStructure = value.asObject();
			final String structureName = dataStructure.get("name").asString();
			final long address = dataStructure.get("address").asLong();
			
			final TranslationLookasideBuffer tlb = holder.getTlb();
			tlb.registerName(structureName, address);
			
			final LookasideTask task = new LookasideTask()
			{				
				@Override
				public void onNotFound(int lookasideId, String name, Address address)
				{
					log.debug("no " + structureName + " structure at " + address.asAddress());					
				}
				
				@Override
				public void execute(int lookasideId, Structure component)
				{
					log.debug("structure '" + structureName + "' loaded");				
				}
			};
			
			final int id = tlb.forComponent(holder, task, AccessMode.READ_ONLY, structureName);
			log.debug("[" + id + "] load structure: name=" + structureName + " address=" + address + "L");
		}	
	}

	private void startServices(String contentType, JsonArray services) throws ServiceException
	{
		log.info("starting services for content type: "  + contentType + " ...");
		
		for (JsonValue value : services)
		{
			final String serviceName = value.asString();
			log.debug("service: " + serviceName);
			
			final Service service = serviceRegistry.lookUp(serviceName);
			
			if (service == null)
			{
				throw new ServiceException("service not registered: " + serviceName);
			}
			else
			{
				log.info(serviceName);
				service.up(properties);
				runningServices.addIfAbsent(serviceName);	
			}			
		}
	}
	
	@Override
	public void down()
	{	
		log.info("bringing down services ...");
		
		for (String serviceName : runningServices)
		{
			final Service service = serviceRegistry.lookUp(serviceName);
			
			if (service == null)
			{
				log.warn("cannot bring down service - not registered: " + serviceName);
			}
			else
			{
				log.info(serviceName);
				service.down();
			}
		}
		
		log.info("services shut down");
	
		pageDaemon.flushAll();
		
		log.info("shutdown complete");
	}
}

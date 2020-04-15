package org.hydroid.beowulf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hydroid.beowulf.overlay.MetaData;
import org.hydroid.beowulf.overlay.Sizing;
import org.hydroid.beowulf.space.SpaceManagementContext;
import org.hydroid.beowulf.space.SpaceManagementContextFactory;
import org.hydroid.beowulf.storage.LocatorFactory;
import org.hydroid.file.Directory;
import org.hydroid.file.FileMode;
import org.hydroid.file.FileSuffixFilter;
import org.hydroid.file.PhysicalResourceException;
import org.hydroid.file.RepositoryFile;
import org.hydroid.page.PageDaemon;
import org.hydroid.page.PageIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.resource.ResourceNotFound;

 
public class LocationManager implements LocationManagerApi {
	public LocationManager(String hostName, Directory location, String suffix, 
			               PageDaemon pageDaemon, SpaceManagementContextFactory spaceManagementContextFactory) {
		this.directory = location;
		this.pageDaemon = pageDaemon;
		this.spaceManagementContextFactory = spaceManagementContextFactory;
		this.locatorFactory = spaceManagementContextFactory.getLocatorFactory();
		
		FilenameFilter filter = new FileSuffixFilter(Arrays.asList(suffix));	
		List<File> files = location.listChildFiles(filter);
		
		// eargerly add a holder for each store
		for (File file : files) {
			String fileName = file.getName();
			String storeName = fileName.substring(0, fileName.indexOf(suffix)); 
			
			addStore(hostName, storeName, suffix, file);
		}
	}

	private void addStore(String hostName, String storeName, String suffix, File file) {
		StoreHolder holder = new StoreHolder(storeName, suffix, file);
		
		// support look-up by host name
		String url = String.format(URL_FORMAT, hostName, storeName);
		urls.put(url, holder);
		
		// support look-up by localhost
		String url2 = String.format(URL_FORMAT, "localhost", storeName);
		urls.put(url2, holder);
	}
	
	@Override
	public void create(String url) throws PhysicalResourceException {
		

	}

	@Override
	public RepositoryManager read(String url) throws PhysicalResourceException {
		StoreHolder holder = ensureStore(url);			
		return ensureManager(holder, FileMode.READ_ONLY);
	}

	@Override
	public void remove(String url) throws PhysicalResourceException {
		StoreHolder holder = ensureStore(url);		
		File file = holder.getFile();
		RepositoryManager manager = holder.getManager();
		
		// just delete the file if we are not currently using it
		if (manager == null) {
			file.delete();
			return;
		}
		
		manager.shutdown();
	}

	@Override
	public void shutDown() throws PhysicalResourceException {
		List<String> failed = new ArrayList<String>();
		
		for (StoreHolder holder : urls.values()) {
			RepositoryManager manager = holder.getManager();
			
			if (null != manager) {
				try {
					manager.shutdown();
				} catch (PhysicalResourceException pre) {
					failed.add(holder.getStoreName());
				}
			}
		}
		
		if (failed.size() > 0) {
			throw new PhysicalResourceException(String.format("could not shutdown: %s", failed.toString()));
		}
	}

	@Override
	public RepositoryManager update(String url) throws PhysicalResourceException {
		StoreHolder holder = ensureStore(url);
		return ensureManager(holder, FileMode.READ_WRITE);
	}
	
	private StoreHolder ensureStore(String url) throws ResourceNotFound {
		StoreHolder holder = urls.get(url);
		
		if (holder == null) {
			throw new ResourceNotFound(url);
		}
		
		return holder;
	}
	 
	private RepositoryManager ensureManager(StoreHolder holder, FileMode fileMode) throws PhysicalResourceException {
		RepositoryManager manager = holder.getManager();
		
		if (manager != null) {
			return manager;
		}
				
		manager = createManager(holder, fileMode);
		holder.setRepositoryManager(manager);
		return manager;
	}			


	private RepositoryManager createManager(StoreHolder holder, FileMode fileMode) throws PhysicalResourceException {
		RepositoryFile repoFile = null;
		
		try {
			repoFile = new RepositoryFile(directory.getFile(), holder.getStoreName(), holder.getSuffix(), fileMode.toString());
		} catch (FileNotFoundException fnfe) {
			throw new ResourceNotFound(holder.getFile().getAbsolutePath());
		}
		
		// read the metadata to discover the block size of this store
		ByteBuffer bb = null;
		
		try {
			bb = repoFile.read(48);
		} catch (IOException e) {
			throw new PhysicalResourceException("could not read meta-data and sizing", e);			
		}
		
		MetaData metadata = new MetaData(bb, locatorFactory);
		logger.debug(metadata.toString());
		
		Sizing sz = new Sizing(bb, locatorFactory);
		PageIdentifier rootPageId = new PageIdentifier(repoFile, 0, sz.getBlockSize());
		SpaceManagementContext spaceManagementContext = spaceManagementContextFactory.make();
		  		
		return new BeowulfRepositoryManager(pageDaemon, repoFile, rootPageId, spaceManagementContext);
	}

	@Override
	public Iterable<String> iterateStoreNames() {	
		Map<String, Boolean> names = new HashMap<String, Boolean>();
		
		for (StoreHolder holder : urls.values()) {
			names.put(holder.getStoreName(), Boolean.TRUE);
		}

		return names.keySet();
	}
	


	private Map<String, StoreHolder> urls = new HashMap<String, StoreHolder>();
	
	private static final String URL_FORMAT = "exop://%s/%s";

	private LocatorFactory locatorFactory;
	private SpaceManagementContextFactory spaceManagementContextFactory;
	private final PageDaemon pageDaemon;
	private final Directory directory;
	
	private static final Logger logger = LoggerFactory.getLogger(LocationManager.class);
}

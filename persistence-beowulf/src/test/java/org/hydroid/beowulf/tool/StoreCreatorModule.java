package org.hydroid.beowulf.tool;

import org.hydroid.beowulf.storage.Block56Slot4Subslot4;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.google.inject.AbstractModule;
import com.lbg.module.PageDaemonModule;

public class StoreCreatorModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LocatorFactory.class).toInstance(new Block56Slot4Subslot4());
		install(new PageDaemonModule());
	}

}

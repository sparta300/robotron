package org.hydroid.beowulf.tool;

import org.hydroid.beowulf.storage.Block56Slot4Subslot4;
import org.hydroid.beowulf.storage.LocatorFactory;

import com.google.inject.AbstractModule;
import com.mfdev.utility.SpringContextBuilder;
import com.mfdev.utility.SpringContextBuilderImpl;

public class ListLauncherModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LocatorFactory.class).toInstance(new Block56Slot4Subslot4());
		bind(SpringContextBuilder.class).toInstance(new SpringContextBuilderImpl());
	}
	

	
	

}

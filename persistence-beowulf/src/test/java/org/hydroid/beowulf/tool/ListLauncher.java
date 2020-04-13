package org.hydroid.beowulf.tool;

import static com.google.inject.Guice.createInjector;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hydroid.beowulf.storage.general.ApiContext;
import org.hydroid.ui.module.FrameworkModule;

import com.google.inject.Injector;

public class ListLauncher {
	private static final Logger log = Logger.getLogger(ListLauncher.class);
	private final ApiContext apiContext;
	
	@Inject
	private ListLauncher(ApiContext apiContext) {
		this.apiContext = apiContext;
	}
		
	public void launch() {		
		log.debug(apiContext.toString());
		log.info("main thread says goodbye");
	}


	public static void main(String... args) {
		// pass in the name of the nv file from the runner arguments, so we can plug it into the gui framework module
		final String resourceName = args[0];
		Injector injector = createInjector(new FrameworkModule(resourceName), new ListLauncherModule());
		final ListLauncher launcher = injector.getInstance(ListLauncher.class);
		launcher.launch();
	}
}

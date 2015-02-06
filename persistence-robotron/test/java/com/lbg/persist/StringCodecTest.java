package com.lbg.persist;

import static com.google.inject.Guice.createInjector;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.lbg.module.CoreModule;
import com.lbg.module.PropertyModule;

public class StringCodecTest
{
	@Test
	public void testEncoder() throws PersistenceException
	{
		final String text = "Shall I compare thee to a summer's day?  Thou art more lovely and more temperate";
		final StringCodec codec = getInstance(StringCodec.class);
		final ByteBuffer bb = codec.encode(text);
		final String decoded = codec.decode(bb);
		assertEquals(text, decoded);
	}
	
	@SuppressWarnings("unchecked")
	private <T> T getInstance(Class<?> klass)
	{
		final List<Module> modules = new ArrayList<Module>();
		modules.add(new CoreModule());
		modules.add(new PropertyModule("test/resource/codec.properties"));
		final Injector injector = createInjector(modules);
		return (T) injector.getInstance(StringCodec.class);
	}
}

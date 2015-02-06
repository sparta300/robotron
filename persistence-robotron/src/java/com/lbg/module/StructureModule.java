package com.lbg.module;

import static com.lbg.persist.structure.StructureType.BLOCK_MAIN;
import static com.lbg.persist.structure.StructureType.COUNT_16;
import static com.lbg.persist.structure.StructureType.GEOMETRY;
import static com.lbg.persist.structure.StructureType.HEADER;
import static com.lbg.persist.structure.StructureType.LIST_16;
import static com.lbg.persist.structure.StructureType.LIST_BOOLEAN_8;
import static com.lbg.persist.structure.StructureType.LIST_DOUBLE;
import static com.lbg.persist.structure.StructureType.LIST_INTEGER;
import static com.lbg.persist.structure.StructureType.LIST_TELEMETRY_FRAME;
import static com.lbg.persist.structure.StructureType.MAGIC;
import static com.lbg.persist.structure.StructureType.RELATIONSHIP_SET;
import static com.lbg.persist.structure.StructureType.STORE_MAIN;
import static com.lbg.persist.structure.StructureType.STRING_DATA;
import static com.lbg.persist.structure.StructureType.TELEMETRY_FRAME;
import static com.lbg.persist.structure.StructureType.TELEMETRY_STREAM;
import static com.lbg.persist.structure.StructureType.VERSION;
import static com.lbg.persist.structure.StructureType.WILDERNESS;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.lbg.persist.structure.StructureFactory;
import com.lbg.persist.structure.StructureFactoryImpl;
import com.lbg.persist.structure.StructureLibrary;
import com.lbg.persist.structure.StructureLibraryImpl;
import com.lbg.persist.structure.StructureReader;
import com.lbg.persist.structure.StructureReaderImpl;
import com.lbg.persist.structure.StructureType;

public class StructureModule extends AbstractModule
{

	@Override
	protected void configure()
	{
		bind(StructureReader.class).to(StructureReaderImpl.class).in(Singleton.class);
		bind(StructureFactory.class).to(StructureFactoryImpl.class).in(Singleton.class);
		bind(StructureLibrary.class).to(StructureLibraryImpl.class).in(Singleton.class);
		
	}

	@Provides @Singleton
	Set<StructureType> provideSupportedStructures()
	{
		final Set<StructureType> set = new HashSet<StructureType>();
		// basic types
		set.add(MAGIC);
		set.add(VERSION);
		set.add(HEADER);
		set.add(WILDERNESS);
		set.add(GEOMETRY);
		set.add(BLOCK_MAIN);
		set.add(STORE_MAIN);
		
		// types that have headers
		set.add(LIST_BOOLEAN_8);
		set.add(LIST_DOUBLE);
		set.add(LIST_INTEGER);
		set.add(COUNT_16);
		set.add(LIST_16);
		set.add(TELEMETRY_STREAM);
		set.add(TELEMETRY_FRAME);
		set.add(LIST_TELEMETRY_FRAME);
		set.add(RELATIONSHIP_SET);
		set.add(STRING_DATA);
		return set;		
	}
}

package com.lbg.persist.creator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * junit test cases for {@link ManifestBuilder}.
 * 
 * @author C006011
 */
public class ManifestBuilderTest
{
	@Test
	public void contentTypeOnly()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.withContentType("telemetry-stream");
		assertEquals("{\"content-type\":\"telemetry-stream\"}", mb.build());
	}
	
	@Test
	public void oneAttributeNumberInteger()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.withAttribute("a1", 10);
		assertEquals("{\"a1\":10}", mb.build());
	}
	
	@Test
	public void oneAttributeNumberLong()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.withAttribute("a1", 10L);
		assertEquals("{\"a1\":10}", mb.build());
	}	
	
	@Test
	public void oneAttributeString()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.withAttribute("a1", "hello");
		assertEquals("{\"a1\":\"hello\"}", mb.build());
	}		
	
	@Test
	public void oneService()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.addService("s1");
		assertEquals("{\"services\":[\"s1\"]}", mb.build());
	}
	
	@Test
	public void twoServices()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.addService("s1");
		mb.addService("s2");		
		assertEquals("{\"services\":[\"s1\",\"s2\"]}", mb.build());
	}
	
	@Test
	public void oneStructure()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.addStructure("s1", 80L);
		assertEquals("{\"structures\":[{\"name\":\"s1\",\"address\":80}]}", mb.build());
	}
	
	@Test
	public void twoStructures()
	{
		final ManifestBuilder mb = new ManifestBuilder();
		mb.addStructure("s1", 80L);
		mb.addStructure("s2", 90L);		
		assertEquals("{\"structures\":[{\"name\":\"s1\",\"address\":80},{\"name\":\"s2\",\"address\":90}]}", mb.build());
	}	
}

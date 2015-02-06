package com.lbg.persist.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lbg.persist.Address;
import com.lbg.persist.structure.Structure;

public class LookasideTaskHolder implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(LookasideTaskHolder.class);
	
	private final AccessMode mode;
	private final int lookasideId;
	private final LookasideTask task;
	private final Structure structure;
	private final String name;
	private final Address address;

	private LookasideTaskHolder(AccessMode mode, int lookasideId, LookasideTask task, Structure structure, String name, Address address)
	{
		this.mode = mode;
		this.lookasideId = lookasideId;
		this.task = task;
		this.structure = structure;
		this.name = name;
		this.address = address;
	}
		
	/**
	 * constructor to be used when a look up by name has failed.
	 */
	public LookasideTaskHolder(AccessMode mode, int lookasideId, LookasideTask task, String name)
	{
		this(mode, lookasideId, task, null, name, null);
	}

	public LookasideTaskHolder(AccessMode mode, int lookasideId, LookasideTask task, Structure structure, Address address)
	{
		this(mode, lookasideId, task, structure, null, address);
	}

	/**
	 * constructor to be used when a look up by component ID has failed.
	 */
	public LookasideTaskHolder(AccessMode mode, int lookasideId, LookasideTask task, Address address)
	{
		this(mode, lookasideId, task, null, null, address);
	}

	@Override
	public void run()
	{
		log.debug("executing look-aside task " + lookasideId + " with mode " + mode.name().toLowerCase());
		
		try
		{
			if (structure != null)
			{
				task.execute(lookasideId, structure);	
			}
			else
			{
				task.onNotFound(lookasideId, name, address);
			}
				
		}
		catch (Throwable t)
		{
			log.error("lookaside task threw an uncaught exception", t);
		}					
	}
}

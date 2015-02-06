package com.lbg.persist.message;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.lbg.persist.message.StorageProtos.FreeListRuntime;



public class ProtoTest
{

	@Test
	public void build()
	{
		final int count = 100;
		final List<Boolean> available = new ArrayList<Boolean>(count);
		
		for (int c = 0; c < count; c++)
		{
			available.add(Boolean.TRUE);
		}
		
		final FreeListRuntime runtime = FreeListRuntime.newBuilder().setTotal(count)
				                                                    .setFree(count)
				                                                    .setUsed(0)
				                                                    .addAllAvailable(available)
				                                                    .build();
				                                                    
		assertEquals(count, runtime.getFree());
		assertEquals(count, runtime.getTotal());
		assertEquals(0, runtime.getUsed());
		 
		assertEquals(16, runtime.getSerializedSize());
	}

}

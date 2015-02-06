package com.lbg.persist.engine;

import com.lbg.persist.Address;
import com.lbg.persist.structure.Structure;

public interface LookasideTask
{
	void execute(int lookasideId, Structure component);

	void onNotFound(int lookasideId, String name, Address address);
}

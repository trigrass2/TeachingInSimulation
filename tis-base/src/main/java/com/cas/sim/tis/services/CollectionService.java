package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.Collection;

public interface CollectionService extends BaseService<Collection> {
	boolean checkCollected(Integer rid);
	
	void uncollect(Integer rid);
	
	void collected(Collection collection);
}

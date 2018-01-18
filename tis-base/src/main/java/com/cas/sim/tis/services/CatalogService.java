package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Catalog;

public interface SectionService extends BaseService<Catalog> {
	/**
	 */
	List<Catalog> findSections(int type, Integer upperId);
}

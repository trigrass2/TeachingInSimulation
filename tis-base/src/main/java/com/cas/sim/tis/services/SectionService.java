package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Section;

public interface SectionService extends BaseService<Section> {
	/**
	 */
	List<Section> findSections(int type, Integer upperId);
}

package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.mapper.PreparationResourceMapper;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.vo.PreparationInfo;

@Service
public class PreparationResourceServiceImpl extends AbstractService<PreparationResource> implements PreparationResourceService {

	@Override
	public List<PreparationInfo> findResourcesByPreparationId(Integer pid) {
		PreparationResourceMapper mapper = (PreparationResourceMapper) this.mapper;
		return mapper.findResourcesByPreparationId(pid);
	}

}

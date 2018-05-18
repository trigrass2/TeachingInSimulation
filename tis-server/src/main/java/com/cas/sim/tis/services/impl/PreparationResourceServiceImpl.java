package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.mapper.PreparationResourceMapper;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

@Service
public class PreparationResourceServiceImpl implements PreparationResourceService {
	@Resource
	private PreparationResourceMapper mapper;

	@Override
	public ResponseEntity findResourcesByPreparationId(RequestEntity entity) {
		List<PreparationInfo> result = mapper.findResourcesByPreparationId(entity.getInt("pid"));
		return ResponseEntity.success(result);
	}

}

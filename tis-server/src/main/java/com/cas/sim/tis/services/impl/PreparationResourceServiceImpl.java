package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.PreparationResource;
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

	@Override
	public ResponseEntity findPreparationResourceById(RequestEntity req) {
		int id = req.getInt("id");
		return ResponseEntity.success(mapper.selectByPrimaryKey(id));
	}

	@Override
	public void updatePreparationResource(RequestEntity req) {
		PreparationResource resource = req.getObject("resource", PreparationResource.class);
		mapper.updateByPrimaryKeySelective(resource);
	}

	@Override
	public void savePreparationResources(RequestEntity req) {
		List<PreparationResource> resources = req.getList("resources", PreparationResource.class);
		mapper.insertList(resources);
	}

}

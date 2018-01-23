package com.cas.sim.tis.services.impl;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.mapper.ResourceMapper;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.vo.ResourceInfo;

@Service
public class ResourceServiceImpl extends AbstractService<Resource> implements ResourceService {

	@Override
	public ResourceInfo findResourceInfoByID(int id) {
		ResourceMapper resourceMapper = (ResourceMapper) mapper;
		ResourceInfo result = resourceMapper.selectResourceInfoByID(id);
		return result;
	}
	
}

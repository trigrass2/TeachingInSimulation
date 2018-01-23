package com.cas.sim.tis.services;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.vo.ResourceInfo;

public interface ResourceService extends BaseService<Resource> {
	ResourceInfo findResourceInfoByID(int id);
}

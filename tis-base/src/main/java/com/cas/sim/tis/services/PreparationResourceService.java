package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.vo.PreparationInfo;

public interface PreparationResourceService extends BaseService<PreparationResource> {

	List<PreparationInfo> findResourcesByPreparationId(Integer pid);

}

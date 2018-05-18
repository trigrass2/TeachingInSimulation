package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationResourceService {

	/**
	 * @param pid
	 * @return List
	 */
	ResponseEntity findResourcesByPreparationId(RequestEntity entity);

}

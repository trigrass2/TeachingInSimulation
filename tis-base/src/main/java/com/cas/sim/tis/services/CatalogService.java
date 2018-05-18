package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface CatalogService {

	/**
	 * @param id
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findCatalogsByParentId(RequestEntity entity);
}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationService {

	/**
	 * @param cid
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findPreparationByTaskIdAndCreator(RequestEntity entity);

	/**
	 * @param preparation
	 * @return
	 */
	@ThriftMethod
	ResponseEntity addPreparation(RequestEntity entity);

}

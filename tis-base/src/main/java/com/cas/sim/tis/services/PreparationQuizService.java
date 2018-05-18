package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationQuizService {
	/**
	 * @param pid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findQuizsByPreparationId(RequestEntity entity);

	/**
	 * @param pid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countFreeQuizByPreparationId(RequestEntity entity);

}

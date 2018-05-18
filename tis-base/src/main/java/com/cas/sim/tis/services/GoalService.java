package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface GoalService {

	/**
	 * @param rid
	 * @param type
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findGoalsByRid(RequestEntity entity);

}

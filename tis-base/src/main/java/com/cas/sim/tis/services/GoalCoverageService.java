package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface GoalCoverageService {
	/**
	 * @param rid
	 * @param type
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findGidsByRid(RequestEntity entity);

	/**
	 * @param gid
	 * @param rid
	 * @param type
	 * @param creator
	 */
	@ThriftMethod
	void deleteRelationship(RequestEntity entity);

	/**
	 * @param oid
	 * @param tid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity checkObjectiveCoverage(RequestEntity entity);

	@ThriftMethod
	void saveGoalCoverage(RequestEntity req);
}

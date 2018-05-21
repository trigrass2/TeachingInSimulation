package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface BrokenCaseService {
	/**
	 * 获得所有故障案例集合
	 * @return 故障案例集合
	 */
	@ThriftMethod
	ResponseEntity findBrokenCases();

}

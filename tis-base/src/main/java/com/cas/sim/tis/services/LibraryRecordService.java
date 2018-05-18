package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryRecordService {
	/**
	 * @param pid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findRecordByPublishId(RequestEntity entity);

	/**
	 * @param record
	 * @param answers
	 */
	@ThriftMethod
	void addRecord(RequestEntity entity);

}

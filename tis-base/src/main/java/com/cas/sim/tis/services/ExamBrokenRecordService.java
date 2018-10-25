package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ExamBrokenRecordService {
	/**
	 * 保存故障维修考核结果
	 * @param entity （BrokenRecord 故障维修考核结果）
	 */
	@ThriftMethod
	ResponseEntity saveBrokenRecord(RequestEntity entity);
}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ExamLibraryRecordService {

	/**
	 * 新增答题记录
	 * @param entity （record 考核/练习记录；answers 考核/练习详情记录）
	 */
	@ThriftMethod
	void addRecord(RequestEntity entity);
}

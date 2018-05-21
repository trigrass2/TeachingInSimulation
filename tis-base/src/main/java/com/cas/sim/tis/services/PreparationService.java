package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationService {

	/**
	 * 根据任务编号和创建人获得备课内容
	 * @param entity （cid 任务编号；creator 创建人）
	 * @return 备课信息对象
	 */
	@ThriftMethod
	ResponseEntity findPreparationByTaskIdAndCreator(RequestEntity entity);

	/**
	 * 新增备课
	 * @param entity （preparation 备课信息对象）
	 * @return 备课信息对象
	 */
	@ThriftMethod
	ResponseEntity addPreparation(RequestEntity entity);

}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface BrokenCaseService {
	/**
	 * 根据故障案例编号查询故障案例
	 * @param entity （id 故障案例编号）
	 * @return 故障案例对象
	 */
	@ThriftMethod
	ResponseEntity findBrokenCasesById(RequestEntity entity);

	/**
	 * 获得所有故障案例集合
	 * @return 故障案例集合
	 */
	@ThriftMethod
	ResponseEntity findBrokenCases();

	/**
	 * 根据创建人获得维修案例集合
	 * @param entity （creator 创建人编号）
	 * @return List 维修案例集合
	 */
	@ThriftMethod
	ResponseEntity findBrokenCaseByCreatorId(RequestEntity entity);

	/**
	 * 新增维修案例
	 * @param entity （brokenCase 典型案例对象）
	 * @return 维修案例对象
	 */
	@ThriftMethod
	ResponseEntity saveBrokenCase(RequestEntity entity);

	/**
	 * 修改维修案例
	 * @param entity （brokenCase 维修案例对象）
	 * @return 维修案例对象
	 */
	@ThriftMethod
	void updateBrokenCase(RequestEntity entity);
}

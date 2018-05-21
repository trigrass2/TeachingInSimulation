package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface TypicalCaseService {
	/**
	 * 根据创建人获得典型案例集合
	 * @param entity （creator 创建人编号）
	 * @return List 典型案例集合
	 */
	@ThriftMethod
	ResponseEntity findTypicalCasesByCreatorId(RequestEntity entity);

	/**
	 * 根据典型案例编号查询典型案例
	 * @param entity （id 典型案例编号）
	 * @return 典型案例对象
	 */
	@ThriftMethod
	ResponseEntity findTypicalCasesById(RequestEntity entity);

	/**
	 * 新增典型案例
	 * @param entity （typicalCase 典型案例对象）
	 * @return 典型案例对象
	 */
	@ThriftMethod
	ResponseEntity saveTypicalCase(RequestEntity entity);

	/**
	 * 修改典型案例
	 * @param entity （typicalCase 典型案例对象）
	 * @return 典型案例对象
	 */
	@ThriftMethod
	void updateTypicalCase(RequestEntity entity);
}

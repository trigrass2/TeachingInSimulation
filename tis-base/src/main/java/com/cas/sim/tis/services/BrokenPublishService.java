package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface BrokenPublishService {
	/**
	 * 发布维修案例考核
	 * @param entity （BrokenPublish 维修案例发布记录）
	 * @return 发布记录编号
	 */
	@ThriftMethod
	ResponseEntity publishBrokenCases(RequestEntity entity);

	/**
	 * 根据发布记录编号获得维修案例发布记录对象
	 * @param entity （id 发布记录编号）
	 * @return 发布记录对象
	 */
	@ThriftMethod
	ResponseEntity findBrokenPublishById(RequestEntity entity);

	/**
	 * 获得指定维修案例发布记录的考核情况
	 * @param entity （id 发布记录编号）
	 * @return 学生考核结果集合
	 */
	@ThriftMethod
	ResponseEntity findSubmitStateByBrokenPublishId(RequestEntity entity);
	
	/**
	 * 更新维修案例状态
	 * @param entity（id 发布记录编号）
	 * @return publish 维修案例发布记录
	 */
	@ThriftMethod
	ResponseEntity updateBrokenPublish(RequestEntity entity);
}

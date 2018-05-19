package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface GoalService {

	/**
	 * 根据ASK关联标号查询ASK目标对象
	 * @param rid ASK关联编号
	 * @param type ASK类型
	 * @return ASK目标集合
	 */
	@ThriftMethod
	ResponseEntity findGoalsByRid(RequestEntity entity);

}

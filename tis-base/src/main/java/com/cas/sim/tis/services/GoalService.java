package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface GoalService {

	/**
	 * 根据ASK关联标号查询ASK目标对象
	 * @param entity （rid ASK关联编号；type ASK类型）
	 * @return ASK目标集合
	 */
	@ThriftMethod
	ResponseEntity findGoalsByRid(RequestEntity entity);

	/**
	 * 通过ASK覆盖关联编号与ASK覆盖关联类型获得ASK目标集合
	 * @param entity （relationIds ASK覆盖关联编号；type ASK覆盖关联类型）
	 * @return ASK目标集合
	 */
	@ThriftMethod
	ResponseEntity findGoalsByCoverageRelationIdsAndType(RequestEntity req);

}

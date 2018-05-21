package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface GoalCoverageService {
	/**
	 * 根据关联编号与关联类型获得相关的目标ASK编号集合
	 * @param entity （rid 关联编号；type 关联类型）
	 * @return 目标ASK编号集合(id)
	 */
	@ThriftMethod
	ResponseEntity findGoalIdsByRid(RequestEntity entity);

	/**
	 * 根据条件删除目标ASK关系表信息（物理删除）
	 * @param entity （gid 目标ASK编号；rid 关联编号；type 关联类型；creator 关系创建人）
	 */
	@ThriftMethod
	void deleteRelationship(RequestEntity entity);

	/**
	 * 验证目标O是否被任务ASK覆盖
	 * @param entity （oid 目标O编号；tid 任务编号）
	 * @return 返回boolean是否覆盖
	 */
	@ThriftMethod
	ResponseEntity checkObjectiveCoverage(RequestEntity entity);

	/**
	 * 保存目标ASK覆盖关系
	 * @param entity （coverage 目标ASK覆盖关系对象）
	 */
	@ThriftMethod
	void saveGoalCoverage(RequestEntity req);
}

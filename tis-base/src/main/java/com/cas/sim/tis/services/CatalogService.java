package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface CatalogService {

	/**
	 * 根据父节点获取子节点集合
	 * @param entity （parentId 父节点编号）
	 * @return 指定父节点下的子节点集合（id,name,type,lessons,rid）
	 */
	@ThriftMethod
	ResponseEntity findCatalogsByParentId(RequestEntity entity);
}

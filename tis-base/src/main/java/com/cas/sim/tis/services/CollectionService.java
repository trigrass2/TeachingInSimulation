package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface CollectionService {
	/**
	 * @param rid 资源编号
	 * @return 返回boolean是否收藏
	 */
	@ThriftMethod
	ResponseEntity checkCollected(RequestEntity entity);
}

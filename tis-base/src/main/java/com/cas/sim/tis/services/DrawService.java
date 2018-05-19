package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface DrawService {
	/**
	 * 根据条件查询图纸信息集合
	 * @param creator 上传图纸用户编号，为null则查询所有未逻辑删除的图纸信息
	 * @return 返回图纸信息集合(id, name, paths, createDate)
	 */
	@ThriftMethod
	ResponseEntity findDraws(RequestEntity entity);
}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationResourceService {

	/**
	 * 根据备课编号获得备课资源集合
	 * @param entity （pid 备课编号）
	 * @return List PreparationInfo集合
	 */
	@ThriftMethod
	ResponseEntity findResourcesByPreparationId(RequestEntity entity);

	/**
	 * 根据备课资源编号获得备课资源对象
	 * @param req （id 备课资源编号）
	 * @return 备课资源对象
	 */
	@ThriftMethod
	ResponseEntity findPreparationResourceById(RequestEntity req);

	/**
	 * 修改备课资源对象信息
	 * @param req （resource 备课资源对对象）
	 */
	@ThriftMethod
	void updatePreparationResource(RequestEntity req);

	/**
	 * 新增备课资源对象信息
	 * @param req （resources 备课资源对对象集合）
	 */
	@ThriftMethod
	void savePreparationResources(RequestEntity req);

}

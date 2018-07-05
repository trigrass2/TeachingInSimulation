package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationLibraryService {
	/**
	 * 通过备课编号获得备课试题组集合
	 * @param entity （pid 备课编号）
	 * @return List PreparationInfo集合
	 */
	@ThriftMethod
	ResponseEntity findPreparationLibraryByPreparationId(RequestEntity entity);
	/**
	 * 新增试题组集合
	 * @param entity （pid 备课编号，library 备课试题组）
	 */
	@ThriftMethod
	void addPreparationLibrary(RequestEntity entity);
	/**
	 * 修改试题组集合
	 * @param entity （library 备课试题组）
	 */
	@ThriftMethod
	void updatePreparationLibrary(RequestEntity entity);
	/**
	 * 删除试题组集合（逻辑删除）
	 * @param entity （library 备课试题组）
	 */
	@ThriftMethod
	void deletePreparationLibrary(RequestEntity req);
}

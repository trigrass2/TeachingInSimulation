package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ArchiveCaseService {
	/**
	 * 根据创建人获得存档案例集合
	 * @param entity （creator 创建人编号）
	 * @return List 存档案例集合
	 */
	@ThriftMethod
	ResponseEntity findArchiveCasesByCreatorId(RequestEntity entity);

	/**
	 * 根据存档案例编号查询存档案例
	 * @param entity （id 存档案例编号）
	 * @return 存档案例对象
	 */
	@ThriftMethod
	ResponseEntity findArchiveCasesById(RequestEntity entity);

	/**
	 * 新增存档案例
	 * @param entity （archiveCase 存档案例对象）
	 * @return 存档案例对象
	 */
	@ThriftMethod
	ResponseEntity saveArchiveCase(RequestEntity entity);

	/**
	 * 修改存档案例
	 * @param entity （archiveCase 存档案例对象）
	 * @return 存档案例对象
	 */
	@ThriftMethod
	void updateArchiveCase(RequestEntity entity);
}

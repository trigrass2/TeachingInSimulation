package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationPublishService {
	/**
	 * 发布备课试题组
	 * @param entity （publish 备课试题组对象）
	 * @return 备课试题组发布编号
	 */
	@ThriftMethod
	ResponseEntity publishPreparationLibrary(RequestEntity entity);

	/**
	 * 通过备课试题组编号获得备课试题组
	 * @param entity （id 备课试题组发布编号）
	 * @return 备课试题组对象
	 */
	@ThriftMethod
	ResponseEntity findPreparationPublishById(RequestEntity entity);

	/**
	 * 通过备课试题组编号获得当前正在考核的考试状态
	 * @param entity （id 备课试题组发布编号）
	 * @return 备课试题组对象
	 */
	@ThriftMethod
	ResponseEntity findSubmitStateByPreparationPublishId(RequestEntity entity);

	/**
	 * 通过备课试题组编号获得当前正在考核的考试状态
	 * @param entity （id 备课试题组发布编号）
	 * @return 备课试题组对象
	 */
	@ThriftMethod
	ResponseEntity updatePreparationPublish(RequestEntity entity);
}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface PreparationQuizService {
	/**
	 * 通过备课编号获得备课试题集合
	 * @param entity （pid 备课编号）
	 * @return List PreparationInfo集合
	 */
	@ThriftMethod
	ResponseEntity findQuizsByPreparationId(RequestEntity entity);

	/**
	 * 根据备课编号统计备课自由接线案例的数量
	 * @param entity （pid 备课编号）
	 * @return int 自由接线案例数量
	 */
	@ThriftMethod
	ResponseEntity countFreeQuizByPreparationId(RequestEntity entity);

	/**
	 * 根据备课试题编号获得备课试题对象
	 * @param req （id 备课试题编号）
	 * @return PreparationQuiz 备课试题对象
	 */
	@ThriftMethod
	ResponseEntity findPreparationQuizById(RequestEntity req);

	/**
	 * 保存备课试题对象
	 * @param req （quiz 备课试题对象）
	 */
	@ThriftMethod
	void savePreparationQuiz(RequestEntity req);

}

package com.cas.sim.tis.services;

import javax.annotation.Nonnull;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ElecCompService {
	/**
	 * 获取所有元器件的Map集合<br>
	 * key:元器件型号<br>
	 * value:元器件对象 Map<Integer, List<ElecComp>>
	 * @return
	 */
	@Nonnull
	@ThriftMethod
	ResponseEntity findElecCompGroupByType();

	/**
	 * 根据元器件ID查找
	 * @param id
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findElecCompById(RequestEntity req);

	/**
	 * 根据元器件型号查找
	 * @param model
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findElecCompByModel(RequestEntity entity);

	/**
	 * 获取所有元器件集合
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findElecComps();
}

package com.cas.sim.tis.services;

import javax.annotation.Nonnull;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ElecCompService {
	/**
	 * 获取所有元器件按元器件类型分类的Map集合<br>
	 * @return key:元器件型号<br>
	 *         value:元器件对象 List<ElecComp>
	 */
	@Nonnull
	@ThriftMethod
	ResponseEntity findElecCompGroupByType();

	/**
	 * 根据元器件编号获得的元器件对象
	 * @param id 元器件编号
	 * @return 元器件对象
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

package com.cas.sim.tis.services;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.cas.sim.tis.entity.ElecComp;

public interface ElecCompService extends BaseService<ElecComp> {
	/**
	 * 获取所有元器件的Map集合<br>
	 * key:元器件型号<br>
	 * value:元器件对象
	 * @return
	 */
	@Nonnull
	Map<String, List<ElecComp>> findElecCompGroupByType();

	/**
	 * 根据元器件型号查找
	 * @param model
	 * @return
	 */
	ElecComp findElecCompByModel(String model);
}

package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.services.PreparationService;

@Component
public class PreparationAction extends BaseAction {
	@Reference
	private PreparationService service;

	/**
	 * 根据任务编号和创建人获得备课内容
	 * @param cid
	 * @param creator
	 * @return
	 */
	public Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator) {
		return service.findPreparationByTaskIdAndCreator(cid, creator);
	}

	public Preparation addPreparation(Preparation preparation) {
		return service.addPreparation(preparation);
	}
}

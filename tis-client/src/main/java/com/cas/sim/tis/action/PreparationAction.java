package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.services.PreparationService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class PreparationAction extends BaseAction {
	@Resource
	private PreparationService service;

	/**
	 * 根据任务编号和创建人获得备课内容
	 * @param cid 任务编号
	 * @param creator 创建人
	 * @return 备课信息对象
	 */
	public Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("cid", cid)//
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findPreparationByTaskIdAndCreator(req);
		return JSON.parseObject(resp.data, Preparation.class);
	}

	/**
	 * 新增备课
	 * @param preparation 备课信息对象
	 * @return 备课信息对象
	 */
	public Preparation addPreparation(Preparation preparation) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("preparation", preparation)//
				.build();
		ResponseEntity resp = service.addPreparation(req);
		return JSON.parseObject(resp.data, Preparation.class);
	}
}

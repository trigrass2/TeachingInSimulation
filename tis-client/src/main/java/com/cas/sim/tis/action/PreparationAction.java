package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.services.PreparationService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class PreparationAction extends BaseAction {
	@Resource
	private PreparationService service;

	/**
	 * 根据任务编号和创建人获得备课内容
	 * @param cid
	 * @param creator
	 * @return
	 */
	public Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator) {
		RequestEntity req = new RequestEntity();
		req.set("cid", cid).set("creator", creator).end();
		ResponseEntity resp = service.findPreparationByTaskIdAndCreator(req);
		return JSON.parseObject(resp.data, Preparation.class);
	}

	public Preparation addPreparation(Preparation preparation) {
		RequestEntity req = new RequestEntity();
		req.set("preparation", preparation).end();
		ResponseEntity resp = service.addPreparation(req);
		return JSON.parseObject(resp.data, Preparation.class);
	}
}

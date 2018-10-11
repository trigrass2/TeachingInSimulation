package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrokenPublish;
import com.cas.sim.tis.services.BrokenPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.SubmitInfo;

public class BrokenPublishAction extends BaseAction {
	@Resource
	private BrokenPublishService service;

	public Integer publishBroken(Integer brokenId, Integer classId) {
		BrokenPublish publish = new BrokenPublish();
		publish.setBrokenId(brokenId);
		publish.setClassId(classId);
		publish.setPublisher(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();
		ResponseEntity resp = service.publishBrokenCases(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	public BrokenPublish findPublishById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findBrokenPublishById(req);
		return JSON.parseObject(resp.data, BrokenPublish.class);
	}

	public List<SubmitInfo> findSubmitStateById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findSubmitStateByBrokenPublishId(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}
}

package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamPublish.Type;
import com.cas.sim.tis.services.ExamBrokenPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ExamBrokenPublish;
import com.cas.sim.tis.vo.SubmitInfo;


@Component
public class BrokenPublishAction extends BaseAction {
	@Resource
	private ExamBrokenPublishService service;

	public Integer publishBroken(Integer brokenId, Integer classId) {
		ExamBrokenPublish publish = new ExamBrokenPublish();
		publish.setType(Type.BROKEN_EXAM.getType());
		publish.setRelationId(brokenId);
		publish.setClassId(classId);
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();
		ResponseEntity resp = service.publishBrokenCases(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	public ExamBrokenPublish findPublishById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findBrokenPublishById(req);
		return JSON.parseObject(resp.data, ExamBrokenPublish.class);
	}

	public List<SubmitInfo> findSubmitStateById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findSubmitStateByBrokenPublishId(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}
}

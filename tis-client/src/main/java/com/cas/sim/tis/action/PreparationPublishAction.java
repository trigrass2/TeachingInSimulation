package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.ExamPublish.Type;
import com.cas.sim.tis.services.ExamPreparationPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ExamPreparationPublish;
import com.cas.sim.tis.vo.SubmitInfo;

@Component
public class PreparationPublishAction {
	@Resource
	private ExamPreparationPublishService service;

	public Integer publishPreparationLibrary(Integer rid, Integer cid) {
		ExamPreparationPublish publish = new ExamPreparationPublish();
		publish.setClassId(cid);
		publish.setRelationId(rid);
		publish.setType(Type.PREPARATION_EXAM.getType());
		publish.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();
		ResponseEntity resp = service.publishPreparationLibrary(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	public ExamPreparationPublish findPublishById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPreparationPublishById(req);
		return JSON.parseObject(resp.data, ExamPreparationPublish.class);
	}

	public List<SubmitInfo> findSubmitStateById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findSubmitStateByPreparationPublishId(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}
}

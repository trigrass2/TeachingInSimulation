package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.PreparationPublishType;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.services.PreparationPublishService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.SubmitInfo;

@Component
public class PreparationPublishAction {
	@Resource
	private PreparationPublishService service;

	public Integer publishPreparationLibrary(Integer rid, Integer cid) {
		PreparationPublish publish = new PreparationPublish();
		publish.setClassId(cid);
		publish.setRelationId(rid);
		publish.setType(PreparationPublishType.LIBRARY.getType());
		publish.setPublisher(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("publish", publish)//
				.build();
		ResponseEntity resp = service.publishPreparationLibrary(req);
		return JSON.parseObject(resp.data, Integer.class);
	}

	public PreparationPublish findPublishById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPreparationPublishById(req);
		return JSON.parseObject(resp.data, PreparationPublish.class);
	}

	public List<SubmitInfo> findSubmitStateById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findSubmitStateByPreparationPublishId(req);
		return JSON.parseArray(resp.data, SubmitInfo.class);
	}
}

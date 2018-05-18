package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class CollectionAction extends BaseAction {
	@Resource
	private CollectionService service;

	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		RequestEntity req = new RequestEntity();
		req.set("rid", rid);

		ResponseEntity resp = service.checkCollected(req);
		return JSON.parseObject(resp.data, Boolean.class);
	}

}

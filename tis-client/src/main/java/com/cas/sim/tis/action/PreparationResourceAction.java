package com.cas.sim.tis.action;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationResourceAction extends BaseAction {
	@Resource
	private PreparationResourceService service;

	public List<PreparationInfo> findResourcesByPreparationId(Integer pid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.build();
		ResponseEntity resp = service.findResourcesByPreparationId(req);
		return JSON.parseArray(resp.data, PreparationInfo.class);
	}

	public PreparationResource findResourceById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPreparationResourceById(req);
		return JSON.parseObject(resp.data, PreparationResource.class);
	}

	public void detele(Integer id) {
		PreparationResource resource = new PreparationResource();
		resource.setId(id);
		resource.setDel(true);
		resource.setUpdater(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntityBuilder()//
				.set("resource", resource)//
				.build();
		service.updatePreparationResource(req);
	}

	public void addResources(PreparationResource... resources) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resources", Arrays.asList(resources))//
				.build();
		service.savePreparationResources(req);
	}

	public void addResources(List<PreparationResource> resources) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resources", Arrays.asList(resources))//
				.build();
		service.savePreparationResources(req);
	}

}

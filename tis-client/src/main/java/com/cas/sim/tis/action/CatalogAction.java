package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class CatalogAction extends BaseAction {
	@Resource
	private CatalogService service;

	public List<Catalog> findCatalogsByParentId(Integer parentId) {
		JSONObject data = new JSONObject();
		data.put("parentId", parentId);

		RequestEntity req = new RequestEntity();
		req.data = data.toJSONString();

		ResponseEntity resp = service.findCatalogsByParentId(req);
		return JSON.parseArray(resp.data, Catalog.class);
	}

}

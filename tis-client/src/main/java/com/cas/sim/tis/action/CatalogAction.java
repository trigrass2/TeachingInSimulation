package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class CatalogAction extends BaseAction {
	@Resource
	private CatalogService service;

	/**
	 * @param parentId 父节点编号
	 * @return 指定父节点下的子项目集合（id,name,type,lessons,rid）
	 */
	public List<Catalog> findCatalogsByParentId(Integer parentId) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("parentId", parentId)//
				.build();

		ResponseEntity resp = service.findCatalogsByParentId(req);
		return JSON.parseArray(resp.data, Catalog.class);
	}

}

package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.services.CatalogService;

@Component
public class CatalogAction extends BaseAction {
	@Resource(name = "catalogService")
	private CatalogService service;

	public List<Catalog> findCatalogsByParentId(Integer id) {
		return service.findCatalogsByParentId(id);
	}

}

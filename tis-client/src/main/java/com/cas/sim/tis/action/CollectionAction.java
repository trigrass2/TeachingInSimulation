package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.CollectionService;

@Component
public class CollectionAction extends BaseAction {
	@Resource(name = "collectionService")
	private CollectionService service;

	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		return service.checkCollected(rid);
	}

}

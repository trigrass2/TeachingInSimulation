package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.services.CollectionService;

@Component
public class CollectionAction extends BaseAction {
	@Reference
	private CollectionService service;

	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		return service.checkCollected(rid);
	}

}

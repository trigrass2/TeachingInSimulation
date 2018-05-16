package com.cas.sim.tis.action;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.services.BrokenCaseService;

@Component
public class BrokenCaseAction extends BaseAction {
	@Reference
	private BrokenCaseService service;
	
	public List<BrokenCase> getBrokenCaseList() {
		return service.findAll();
	}
}

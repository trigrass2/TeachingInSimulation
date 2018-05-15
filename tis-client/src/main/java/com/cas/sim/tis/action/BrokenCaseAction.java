package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.services.BrokenCaseService;

@Component
public class BrokenCaseAction extends BaseAction {
	@Resource(name = "brokenCaseService")
	private BrokenCaseService service;

	public List<BrokenCase> getBrokenCaseList() {
		return service.findAll();
	}
}

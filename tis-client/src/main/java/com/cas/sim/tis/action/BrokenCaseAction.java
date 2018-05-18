package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.services.BrokenCaseService;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class BrokenCaseAction extends BaseAction {
	@Resource
	private BrokenCaseService service;

	public List<BrokenCase> getBrokenCaseList() {
		ResponseEntity entity = service.findBrokenCases();
		return JSON.parseArray(entity.data, BrokenCase.class);
	}
}

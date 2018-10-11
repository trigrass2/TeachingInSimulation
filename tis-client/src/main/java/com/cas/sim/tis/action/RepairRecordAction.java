package com.cas.sim.tis.action;

import javax.annotation.Resource;

import com.cas.sim.tis.services.BrokenRecordService;

public class RepairRecordAction extends BaseAction {
	@Resource
	private BrokenRecordService service;
}

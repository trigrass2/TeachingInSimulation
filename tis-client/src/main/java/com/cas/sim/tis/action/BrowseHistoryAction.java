package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.BrowseHistoryService;

@Component
public class BrowseHistoryAction extends BaseAction {
	@Resource
	private BrowseHistoryService service;

}

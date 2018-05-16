package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.services.BrowseHistoryService;

@Component
public class BrowseHistoryAction extends BaseAction {
	@Reference
	private BrowseHistoryService service;

}

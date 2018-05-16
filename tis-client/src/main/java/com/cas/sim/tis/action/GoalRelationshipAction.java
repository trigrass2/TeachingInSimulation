package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.GoalRelationshipService;

@Component
public class GoalRelationshipAction extends BaseAction {

	@Resource(name = "goalRelationshipService")
	private GoalRelationshipService service;

}

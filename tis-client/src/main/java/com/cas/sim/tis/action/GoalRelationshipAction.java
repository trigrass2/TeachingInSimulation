package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class GoalRelationshipAction extends BaseAction {

	@Resource(name = "goalRelationshipService")
	private RmiProxyFactoryBean service;

}

package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.GoalRelationshipService;

@Component
public class GoalRelationshipAction extends BaseAction<GoalRelationshipService> {

	@Resource
	@Qualifier("goalRelationshipServiceFactory")
	private RmiProxyFactoryBean goalRelationshipServiceFactory;

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return goalRelationshipServiceFactory;
	}

}

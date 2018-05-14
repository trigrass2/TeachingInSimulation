package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.GoalRelationship;
import com.cas.sim.tis.services.GoalRelationshipService;
@Component
public class GoalRelationshipAction extends BaseAction<GoalRelationshipService> {

	@Resource
	@Qualifier("goalRelationshipServiceFactory")
	private RmiProxyFactoryBean goalRelationshipServiceFactory;

	public List<GoalRelationship> findGidsByRid(Integer rid, int type) {
		return getService().findGidsByRid(rid, type);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return goalRelationshipServiceFactory;
	}

}

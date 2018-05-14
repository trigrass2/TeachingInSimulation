package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.services.GoalService;

@Component
public class GoalAction extends BaseAction<GoalService> {

	@Resource
	@Qualifier("goalServiceFactory")
	private RmiProxyFactoryBean goalServiceFactory;

	public List<Goal> findGoalsByRid(Integer rid, int type) {
		return getService().findGoalsByRid(rid, type);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return goalServiceFactory;
	}
}

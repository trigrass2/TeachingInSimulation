package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.services.GoalCoverageService;

@Component
public class GoalCoverageAction extends BaseAction<GoalCoverageService> {

	@Resource
	@Qualifier("goalCoverageServiceFactory")
	private RmiProxyFactoryBean goalCoverageServiceFactory;

	public List<GoalCoverage> findGidsByRid(Integer rid, int type) {
		List<GoalCoverage> coverages = getService().findGidsByRid(rid, type);
		if (coverages == null) {
			return new ArrayList<GoalCoverage>();
		} else {
			return coverages;
		}
	}

	public void insertRelationship(Integer gid, Integer rid, int type) {
		GoalCoverage coverage = new GoalCoverage();
		coverage.setGoalId(gid);
		coverage.setRelationId(rid);
		coverage.setType(type);
		coverage.setCreator(Session.get(Session.KEY_LOGIN_ID));
		getService().save(coverage);
	}

	public void deleteRelationship(Integer gid, Integer rid, int type) {
		getService().deleteRelationship(gid, rid, type, Session.get(Session.KEY_LOGIN_ID));
	}

	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		return getService().checkObjectiveCoverage(oid, tid);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return goalCoverageServiceFactory;
	}

}

package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.services.GoalCoverageService;

@Component
public class GoalCoverageAction extends BaseAction {

	@Reference
	private GoalCoverageService service;

	public List<GoalCoverage> findGidsByRid(Integer rid, int type) {
		List<GoalCoverage> coverages = service.findGidsByRid(rid, type);
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
		service.save(coverage);
	}

	public void deleteRelationship(Integer gid, Integer rid, int type) {
		service.deleteRelationship(gid, rid, type, Session.get(Session.KEY_LOGIN_ID));
	}

	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		return service.checkObjectiveCoverage(oid, tid);
	}

}

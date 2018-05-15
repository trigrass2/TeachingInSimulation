package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.GoalCoverage;

public interface GoalCoverageService extends BaseService<GoalCoverage> {

	List<GoalCoverage> findGidsByRid(Integer rid, int type);
	
	void deleteRelationship(Integer gid, Integer rid, int type, Integer creator);

	boolean checkObjectiveCoverage(Integer oid, Integer tid);
}

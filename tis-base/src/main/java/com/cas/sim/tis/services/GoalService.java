package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Goal;

public interface GoalService extends BaseService<Goal> {
	
	List<Goal> findGoalsByRid(Integer rid, int type);

}

package com.cas.sim.tis.services.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.mapper.GoalMapper;
import com.cas.sim.tis.services.GoalService;

@Service
public class GoalServiceImpl extends AbstractService<Goal> implements GoalService {

	@Override
	public List<Goal> findGoalsByRid(Integer rid, int type) {
		return ((GoalMapper)mapper).findGoalsByRid(rid, type);
	}

}

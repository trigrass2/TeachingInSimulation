package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.services.GoalService;

@Component
public class GoalAction extends BaseAction {

	@Resource(name = "goalService")
	private GoalService service;

	public List<Goal> findGoalsByRid(Integer rid, int type) {
		return service.findGoalsByRid(rid, type);
	}
}

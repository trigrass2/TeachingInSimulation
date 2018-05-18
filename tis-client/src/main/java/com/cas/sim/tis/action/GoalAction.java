package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.services.GoalService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class GoalAction extends BaseAction {

	@Resource
	private GoalService service;

	public List<Goal> findGoalsByRid(Integer rid, int type) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid).set("type", type).end();

		ResponseEntity resp = service.findGoalsByRid(req);
		return JSON.parseArray(resp.data, Goal.class);
	}
}

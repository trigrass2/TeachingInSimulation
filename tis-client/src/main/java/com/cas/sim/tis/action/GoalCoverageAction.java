package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.services.GoalCoverageService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class GoalCoverageAction extends BaseAction {

	@Resource
	private GoalCoverageService service;

	public List<GoalCoverage> findGidsByRid(Integer rid, int type) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid);
		req.set("type", type);
		ResponseEntity resp = service.findGidsByRid(req);
		return JSON.parseArray(resp.data, GoalCoverage.class);
	}

	public void insertRelationship(Integer gid, Integer rid, int type) {
		GoalCoverage coverage = new GoalCoverage();
		coverage.setGoalId(gid);
		coverage.setRelationId(rid);
		coverage.setType(type);
		coverage.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntity();
		req.set("coverage", coverage);
		service.saveGoalCoverage(req);
	}

	public void deleteRelationship(Integer gid, Integer rid, int type) {
		RequestEntity req = new RequestEntity();
		req.set("gid", gid);
		req.set("rid", rid);
		req.set("type", type);
		req.set("creator", Session.get(Session.KEY_LOGIN_ID));
		service.deleteRelationship(req);
	}

	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		RequestEntity req = new RequestEntity();
		req.set("oid", oid);
		req.set("tid", tid);
		ResponseEntity resp = service.checkObjectiveCoverage(req);
		return JSON.parseObject(resp.data, Boolean.class);
	}

}

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
		req.set("rid", rid).set("type", type).end();
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
		req.set("coverage", coverage).end();
		service.saveGoalCoverage(req);
	}

	public void deleteRelationship(Integer gid, Integer rid, int type) {
		RequestEntity req = new RequestEntity();
		req.set("gid", gid).set("rid", rid).set("type", type).set("creator", Session.get(Session.KEY_LOGIN_ID)).end();
		service.deleteRelationship(req);
	}

	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		RequestEntity req = new RequestEntity();
		req.set("oid", oid).set("tid", tid).end();
		ResponseEntity resp = service.checkObjectiveCoverage(req);
		return JSON.parseObject(resp.data, Boolean.class);
	}

}

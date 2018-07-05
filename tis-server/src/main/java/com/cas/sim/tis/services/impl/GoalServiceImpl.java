package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.mapper.GoalMapper;
import com.cas.sim.tis.services.GoalService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Service
public class GoalServiceImpl implements GoalService {

	@Resource
	private GoalMapper mapper;

	@Override
	public ResponseEntity findGoalsByRid(RequestEntity entity) {
		int rid = entity.getInt("rid");
		int type = entity.getInt("type");
		return ResponseEntity.success(mapper.findGoalsByRid(rid, type));
	}

	@Override
	public ResponseEntity findGoalsByCoverageRelationIdsAndType(RequestEntity req) {
		List<String> relationIds = req.getList("relationIds", String.class);
		int type = req.getInt("type");
		List<Goal> goals = mapper.findGoalsByCoverageRelationIdsAndType(relationIds, type);
		return ResponseEntity.success(goals);
	}

}

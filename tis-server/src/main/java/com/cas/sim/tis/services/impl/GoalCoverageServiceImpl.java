package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.mapper.GoalCoverageMapper;
import com.cas.sim.tis.services.GoalCoverageService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class GoalCoverageServiceImpl implements GoalCoverageService {

	@Resource
	private GoalCoverageMapper mapper;

	@Override
	public ResponseEntity findGoalIdsByRid(RequestEntity entity) {
		int rid = entity.getInt("rid");
		int type = entity.getInt("type");
		
		Condition condition = new Condition(GoalCoverage.class);
		condition.selectProperties("goalId");
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relationId", rid);
		criteria.andEqualTo("type", type);
		return ResponseEntity.success(mapper.selectByCondition(condition));
	}

	@Override
	public void deleteRelationship(RequestEntity entity) {
		int gid = entity.getInt("gid");
		int rid = entity.getInt("rid");
		int type = entity.getInt("type");
		int creator = entity.getInt("creator");
		
		Condition condition = new Condition(GoalCoverage.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("goalId", gid);
		criteria.andEqualTo("relationId", rid);
		criteria.andEqualTo("type", type);
		criteria.andEqualTo("creator", creator);
		mapper.deleteByCondition(condition);
	}

	@Override
	public ResponseEntity checkObjectiveCoverage(RequestEntity entity) {
		int oid = entity.getInt("oid");
		int tid = entity.getInt("tid");
		return ResponseEntity.success(mapper.checkObjectiveCoverage(oid, tid));
	}

	@Override
	public void saveGoalCoverage(RequestEntity req) {
		GoalCoverage coverage = req.getObject("coverage", GoalCoverage.class);
		mapper.insertSelective(coverage);
	}
}

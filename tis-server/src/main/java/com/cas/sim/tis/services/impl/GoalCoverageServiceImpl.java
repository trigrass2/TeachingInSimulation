package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.GoalCoverage;
import com.cas.sim.tis.mapper.GoalCoverageMapper;
import com.cas.sim.tis.services.GoalCoverageService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class GoalCoverageServiceImpl extends AbstractService<GoalCoverage> implements GoalCoverageService {

	@Override
	public List<GoalCoverage> findGidsByRid(Integer rid, int type) {
		Condition condition = new Condition(GoalCoverage.class);
		condition.selectProperties("goalId");
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relationId", rid);
		criteria.andEqualTo("type", type);
		return mapper.selectByCondition(condition);
	}
	
	@Override
	public void deleteRelationship(Integer gid, Integer rid, int type, Integer creator) {
		Condition condition = new Condition(GoalCoverage.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("goalId", gid);
		criteria.andEqualTo("relationId", rid);
		criteria.andEqualTo("type", type);
		criteria.andEqualTo("creator", creator);
		mapper.deleteByCondition(condition);
	}

	@Override
	public boolean checkObjectiveCoverage(Integer oid, Integer tid) {
		GoalCoverageMapper mapper = (GoalCoverageMapper) this.mapper;
		return mapper.checkObjectiveCoverage(oid, tid);
	}
}

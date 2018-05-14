package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.GoalRelationship;
import com.cas.sim.tis.services.GoalRelationshipService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class GoalRelationshipServiceImpl extends AbstractService<GoalRelationship> implements GoalRelationshipService {

	@Override
	public List<GoalRelationship> findGidsByRid(Integer rid, int type) {
		Condition condition = new Condition(GoalRelationship.class);
		condition.selectProperties("id");
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relationId", rid);
		criteria.andEqualTo("type", type);
		criteria.andEqualTo("del", 0);
		return mapper.selectByCondition(condition);
	}
}

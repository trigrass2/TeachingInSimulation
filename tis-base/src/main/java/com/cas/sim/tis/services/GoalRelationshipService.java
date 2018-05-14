package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.GoalRelationship;

public interface GoalRelationshipService extends BaseService<GoalRelationship> {

	List<GoalRelationship> findGidsByRid(Integer rid, int type);

}

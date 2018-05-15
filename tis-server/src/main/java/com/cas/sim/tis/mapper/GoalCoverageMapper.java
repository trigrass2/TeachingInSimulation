package com.cas.sim.tis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.GoalCoverage;

@Mapper
public interface GoalCoverageMapper extends IMapper<GoalCoverage> {

	boolean checkObjectiveCoverage(@Param("oid") Integer oid, @Param("tid") int tid);

}

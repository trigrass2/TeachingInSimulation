package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Goal;

@Mapper
public interface GoalMapper extends IMapper<Goal> {

	List<Goal> findGoalsByRid(@Param("rid") Integer rid, @Param("type") int type);

}

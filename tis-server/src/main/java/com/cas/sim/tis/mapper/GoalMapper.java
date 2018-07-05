package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Goal;

@Mapper
public interface GoalMapper extends IMapper<Goal> {

	/**
	 * 根据ASK关联标号查询ASK目标对象
	 * @param rid ASK关联编号
	 * @param type ASK类型
	 * @return ASK目标集合
	 */
	List<Goal> findGoalsByRid(@Param("rid") Integer rid, @Param("type") int type);

	/**
	 * 通过ASK覆盖关联编号与ASK覆盖关联类型获得ASK目标集合
	 * @param relationIds ASK覆盖关联编号
	 * @param type ASK覆盖关联类型）
	 * @return ASK目标集合
	 */
	List<Goal> findGoalsByCoverageRelationIdsAndType(@Param("relationIds") List<String> relationIds, @Param("type") int type);

}

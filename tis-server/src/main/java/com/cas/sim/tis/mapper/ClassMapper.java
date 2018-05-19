package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.vo.ClassInfo;

@Mapper
public interface ClassMapper extends IMapper<Class> {
	/**
	 * 根据导入信息新增班级信息（通过教师工号获得用户信息编号）
	 * @param infos
	 * @param creator
	 */
	void insertClasses(@Param("infos") List<ClassInfo> infos, @Param("creator") Integer creator);

	/**
	 * 查询班级信息并关联负责教师信息
	 * @return
	 */
	List<Class> findClasses();

}

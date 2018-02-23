package com.cas.sim.tis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.vo.ClassInfo;

@Mapper
public interface ClassMapper extends IMapper<Class> {

	void insertClasses(@Param("infos") List<ClassInfo> infos, @Param("creator") Integer creator);

	List<Class> findClasses();

}

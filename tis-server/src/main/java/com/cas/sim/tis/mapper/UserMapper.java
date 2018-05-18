package com.cas.sim.tis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.User;

import tk.mybatis.mapper.common.example.SelectOneByExampleMapper;

@Mapper
public interface UserMapper extends IMapper<User>, SelectOneByExampleMapper<User> {

	void updateTeacherIdByClassId(@Param("classId") Integer classId, @Param("teacherId") Integer teacherId);

}

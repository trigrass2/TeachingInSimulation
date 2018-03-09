package com.cas.sim.tis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cas.sim.tis.entity.User;

@Mapper
public interface UserMapper extends IMapper<User> {

	void updateTeacherIdByClassId(@Param("classId")Integer classId, @Param("teacherId")Integer teacherId);

}

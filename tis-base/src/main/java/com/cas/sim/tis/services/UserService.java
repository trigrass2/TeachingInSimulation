package com.cas.sim.tis.services;

import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface UserService {
	/**
	 * 用户登录
	 * @param usercode 用户账号
	 * @param password 用户密码（明文）
	 * @return
	 * @throws ServiceException
	 */
	@ThriftMethod
	ResponseEntity login(RequestEntity entity);

	/**
	 * 查询所有教师信息
	 * @param entity （pageIndex 查询页；pageSize 查询条数）
	 * @return 教师信息集合
	 */
	@ThriftMethod
	ResponseEntity findTeachers(RequestEntity entity);

	/**
	 * 按班级查询所有学生信息
	 * @param entity （pageIndex 查询页；pageSize 查询条数；classId 班级编号）
	 * @return 学生信息集合
	 */
	@ThriftMethod
	ResponseEntity findStudents(RequestEntity entity);

//	void updateTeacherIdByClassId(Class claszz);
	/**
	 * 根据用户编号查询用户对象
	 * @param entity （id 用户编号）
	 * @return 用户对象
	 */
	@ThriftMethod
	ResponseEntity findUserById(RequestEntity entity);

	@ThriftMethod
	void saveUsers(RequestEntity entity);

	@ThriftMethod
	void updateUser(RequestEntity entity);
}

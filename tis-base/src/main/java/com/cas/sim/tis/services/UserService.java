package com.cas.sim.tis.services;

import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftException;
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
	@ThriftMethod(exception = { @ThriftException(type = ServiceException.class, id = 1) })
	ResponseEntity login(RequestEntity entity) throws ServiceException;

	@ThriftMethod
	ResponseEntity findTeachers(RequestEntity entity);

	/**
	 * @param pageIndex
	 * @param pageSize
	 * @param classId
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findStudents(RequestEntity entity);

//	void updateTeacherIdByClassId(Class claszz);

	@ThriftMethod
	ResponseEntity findUserById(RequestEntity entity);

	void saveUsers(RequestEntity entity);

	void updateUser(RequestEntity entity);

	void deleteUser(RequestEntity entity);
}

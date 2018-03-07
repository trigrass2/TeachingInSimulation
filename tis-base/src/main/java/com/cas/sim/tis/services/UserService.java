package com.cas.sim.tis.services;

import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.exception.ServiceException;
import com.github.pagehelper.PageInfo;

public interface UserService extends BaseService<User> {
	/**
	 * 用户登录
	 * @param usercode 用户账号
	 * @param password 用户密码（明文）
	 * @return 
	 * @throws ServiceException
	 * @throws TooManyResultsException
	 */
	User login(String usercode, String password) throws ServiceException, TooManyResultsException;

	List<User> findTeachers();

	PageInfo<User> findTeachers(int pageIndex, int pageSize);

	PageInfo<User> findStudents(int pageIndex, int pageSize, int classId);

	void updateTeacherIdByClassId(Class claszz);

}

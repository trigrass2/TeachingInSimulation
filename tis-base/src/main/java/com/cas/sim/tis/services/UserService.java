package com.cas.sim.tis.services;

import org.apache.ibatis.exceptions.TooManyResultsException;

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

	PageInfo<User> findUsersByRole(int pageIndex, int pageSize, int role);

}

package com.cas.sim.tis.services;

import org.apache.ibatis.exceptions.TooManyResultsException;

import com.cas.sim.tis.entity.User;

public interface UserService {
	/**
	 * 用户登录
	 * @param usercode 用户账号
	 * @param password 用户密码（明文）
	 * @return 
	 * @throws ServiceException
	 * @throws TooManyResultsException
	 */
	User login(String usercode, String password) throws ServiceException, TooManyResultsException;
}

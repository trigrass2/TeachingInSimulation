package com.cas.sim.tis.services;

import org.apache.ibatis.exceptions.TooManyResultsException;

import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.impl.ServiceException;

public interface UserService {
	User login(String usercode, String password) throws ServiceException, TooManyResultsException;
}

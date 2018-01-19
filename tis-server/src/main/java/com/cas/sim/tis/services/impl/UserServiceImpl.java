package com.cas.sim.tis.services.impl;

import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ServiceException;
import com.cas.sim.tis.services.UserService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("userService")
public class UserServiceImpl extends AbstractService<User> implements UserService {
	@Override
	public User login(String usercode, String password) {
		Condition condition = new Condition(User.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("code", usercode);
		criteria.andEqualTo("password", password);
		criteria.andEqualTo("del", 0);
		List<User> user = mapper.selectByCondition(condition);
		if (user.size() == 1) {
			return user.get(0);
		} else if (user.size() == 0) {
			throw new ServiceException("用户名或密码错误！");
		} else {
			throw new TooManyResultsException();
		}
	}
}

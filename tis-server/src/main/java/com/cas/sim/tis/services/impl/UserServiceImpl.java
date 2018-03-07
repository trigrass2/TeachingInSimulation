package com.cas.sim.tis.services.impl;

import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.services.exception.ServerException;
import com.cas.sim.tis.services.exception.ServiceException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

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
		List<User> user = null;
		try {
			user = mapper.selectByCondition(condition);
		} catch (Exception e) {
			throw new ServerException("服务器异常", e);
		}
		if (user.size() == 1) {
			return user.get(0);
		} else if (user.size() == 0) {
			throw new ServiceException("用户名或密码错误！");
		} else {
			throw new TooManyResultsException();
		}
	}

	@Override
	public List<User> findTeachers() {
		Condition condition = new Condition(User.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("role", RoleConst.TEACHER);
		criteria.andEqualTo("del", 0);
		condition.orderBy("createDate").desc();
		return findByCondition(condition);
	}

	@Override
	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		Condition condition = new Condition(User.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("role", RoleConst.TEACHER);
		criteria.andEqualTo("del", 0);
		condition.orderBy("createDate").desc();
		PageHelper.startPage(pageIndex, pageSize);
		List<User> result = findByCondition(condition);
		PageInfo<User> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		Condition condition = new Condition(User.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("role", RoleConst.STUDENT);
		criteria.andEqualTo("classId", classId);
		criteria.andEqualTo("del", 0);
		condition.orderBy("createDate").desc();
		PageHelper.startPage(pageIndex, pageSize);
		List<User> result = findByCondition(condition);
		PageInfo<User> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public void updateTeacherIdByClassId(Class clazz) {
		UserMapper mapper = (UserMapper) this.mapper;
		mapper.updateTeacherIdByClassId(clazz.getId(), clazz.getTeacherId());
	}
}

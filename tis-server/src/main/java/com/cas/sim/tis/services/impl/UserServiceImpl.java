package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.RoleConst;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.services.exception.ServiceException;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper mapper;

	@Override
	public ResponseEntity login(RequestEntity entity) throws ServiceException {
		Condition condition = new Condition(User.class);
		condition.createCriteria()//
				.andEqualTo("code", entity.getString("usercode"))//
				.andEqualTo("password", entity.getString("password"))//
				.andEqualTo("del", 0);
		User user = null;
		user = mapper.selectOneByExample(condition);
		if (user == null) {
			throw new ServiceException("用户名或密码错误！");
		}
		return ResponseEntity.success(user);
	}

	@Override
	public ResponseEntity findTeachers(RequestEntity entity) {
		if (entity != null && entity.pageNum != -1) {
//			分页
			PageHelper.startPage(entity.pageNum, entity.pageSize);
		}

		Condition condition = new Condition(User.class);
		condition.createCriteria()//
				.andEqualTo("role", RoleConst.TEACHER)//
				.andEqualTo("del", 0);//
		condition.orderBy("createDate").desc();

		List<User> result = mapper.selectByCondition(condition);
		if (entity != null && entity.pageNum != -1) {
			PageInfo<User> page = new PageInfo<>(result);
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findStudents(RequestEntity entity) {

		Condition condition = new Condition(User.class);
		condition.createCriteria()//
				.andEqualTo("role", RoleConst.STUDENT)//
				.andEqualTo("classId", entity.getInt("classId"))//
				.andEqualTo("del", 0);
		condition.orderBy("createDate").desc();

		if (entity.pageNum != -1) {
			PageHelper.startPage(entity.pageNum, entity.pageSize);
		}
		List<User> result = mapper.selectByCondition(condition);
		if (entity.pageNum != -1) {
			PageInfo<User> page = new PageInfo<>(result);
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findUserById(RequestEntity entity) {
		User user = mapper.selectByPrimaryKey(entity.getInt("id"));
		return ResponseEntity.success(user);
	}

	@Override
	public void saveUsers(RequestEntity entity) {
		List<User> users = entity.getList("users", User.class);
		mapper.insertList(users);
	}

	@Override
	public void updateUser(RequestEntity entity) {
		User user = entity.getObject("user", User.class);
		mapper.updateByPrimaryKeySelective(user);
	}
}

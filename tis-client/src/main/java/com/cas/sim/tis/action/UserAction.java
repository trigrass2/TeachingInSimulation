package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class UserAction extends BaseAction {
	@Resource
	private UserService service;

	public User findUserByID(int id) {
		RequestEntity entity = new RequestEntity().set("id", id).end();
		ResponseEntity resp = service.findUserById(entity);
		return JSON.parseObject(resp.data, User.class);
	}

	public List<User> findTeachers() {
		ResponseEntity resp = service.findTeachers(null);
		return JSON.parseArray(resp.data, User.class);
	}

	public List<User> findTeachers(int pageIndex, int pageSize) {
		RequestEntity entity = new RequestEntity();
		entity.pageNum = pageIndex;
		entity.pageSize = pageSize;
		ResponseEntity resp = service.findTeachers(entity);
		return JSON.parseArray(resp.data, User.class);
	}

	public List<User> findStudents(int pageIndex, int pageSize, int classId) {
		RequestEntity entity = new RequestEntity();
		entity.pageNum = pageIndex;
		entity.pageSize = pageSize;
		entity.set("classId", classId).end();
		ResponseEntity resp = service.findStudents(entity);
		return JSON.parseArray(resp.data, User.class);
	}

	public void addUsers(List<User> users) {
		RequestEntity entity = new RequestEntity();
		entity.set("users", users).end();
		service.saveUsers(entity);
	}

	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity entity = new RequestEntity();
		entity.set("user", user).end();
		service.updateUser(entity);
	}

	public void deleteUser(int id) {
		RequestEntity entity = new RequestEntity();
		entity.set("id", id).end();
		service.deleteUser(entity);
	}

}

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
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		ResponseEntity resp = service.findTeachers(req);
		return JSON.parseArray(resp.data, User.class);
	}

	public List<User> findStudents(int pageIndex, int pageSize, int classId) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		req.set("classId", classId).end();
		ResponseEntity resp = service.findStudents(req);
		return JSON.parseArray(resp.data, User.class);
	}

	public void addUsers(List<User> users) {
		RequestEntity req = new RequestEntity();
		req.set("users", users).end();
		service.saveUsers(req);
	}

	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntity();
		req.set("user", user).end();
		service.updateUser(req);
	}

	public void deleteUser(int id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		service.deleteUser(req);
	}

}

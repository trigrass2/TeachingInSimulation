package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageInfo;

@Component
public class UserAction extends BaseAction {
	@Resource
	private UserService service;

	public User findUserByID(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();

		ResponseEntity resp = service.findUserById(req);
		return JSON.parseObject(resp.data, User.class);
	}

	public List<User> findTeachers() {
		ResponseEntity resp = service.findTeachers(null);
		return JSON.parseArray(resp.data, User.class);
	}

	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.build();
		ResponseEntity resp = service.findTeachers(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<User>>() {});
	}

	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.set("classId", classId)//
				.build();
		ResponseEntity resp = service.findStudents(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<User>>() {});
	}

	public void addUsers(List<User> users) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("users", users)//
				.build();
		service.saveUsers(req);
	}

	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("user", user)//
				.build();
		service.updateUser(req);
	}

	public void deleteUser(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		service.deleteUser(req);
	}

}

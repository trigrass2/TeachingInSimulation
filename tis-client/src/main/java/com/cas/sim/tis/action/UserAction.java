package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class UserAction extends BaseAction {
	@Resource
	private UserService service;

//	public int getTeacherIdByStudentId(Integer studentId) {
//		User user = service.findById(studentId);
//		return user.getTeacherId();
//	}

	public User findUserByID(int id) {
		RequestEntity entity = new RequestEntity();
		JSONObject obj = new JSONObject();
		obj.put("id", id);
		entity.data = obj.toJSONString();

		ResponseEntity resp = service.findUserById(entity);

		return JSON.parseObject(resp.data, User.class);
	}

	public List<User> findTeachers() {
		return service.findTeachers();
	}

	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		return service.findTeachers(pageIndex, pageSize);
	}

	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		return service.findStudents(pageIndex, pageSize, classId);
	}

	public void addUsers(List<User> users) {
		service.save(users);
	}

	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(user);
	}

	public void deleteUser(int id) {
		User user = service.findById(id);
		if (user != null) {
			user.setDel(true);
			modifyUser(user);
		}
	}

}

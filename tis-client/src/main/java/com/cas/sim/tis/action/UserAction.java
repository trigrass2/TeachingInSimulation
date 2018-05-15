package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.github.pagehelper.PageInfo;

@Component
public class UserAction extends BaseAction {
	@Resource(name = "userService")
	private UserService service;

	public int getTeacherIdByStudentId(Integer studentId) {
		User user = service.findById(studentId);
		return user.getTeacherId();
	}

	public User findUserByID(int id) {
		return service.findById(id);
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

package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.github.pagehelper.PageInfo;

@Component
public class UserAction extends BaseAction<UserService> {
	@Resource
	@Qualifier("userServiceFactory")
	private RmiProxyFactoryBean userServiceFactory;

	public int getTeacherIdByStudentId(Integer studentId) {
		User user = getService().findById(studentId);
		return user.getTeacherId();
	}

	public User findUserByID(int id) {
		return getService().findById(id);
	}

	public List<User> findTeachers() {
		return getService().findTeachers();
	}

	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		return getService().findTeachers(pageIndex, pageSize);
	}

	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		return getService().findStudents(pageIndex, pageSize, classId);
	}

	public void addUsers(List<User> users) {
		getService().save(users);
	}

	public void modifyUser(User user) {
		user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		getService().update(user);
	}

	public void deleteUser(int id) {
		UserService service = getService();
		User user = service.findById(id);
		if (user != null) {
			user.setDel(true);
			modifyUser(user);
		}
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return userServiceFactory;
	}

}

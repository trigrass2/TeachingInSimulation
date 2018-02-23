package com.cas.sim.tis.view.action;

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
public class UserAction {
	@Resource
	@Qualifier("userServiceFactory")
	private RmiProxyFactoryBean userServiceFactory;

	public int getTeacherIdByStudentId(Integer studentId) {
		UserService service = (UserService) userServiceFactory.getObject();
		User user = service.findById(studentId);
		return user.getTeacherId();
	}

	public User findUserByID(int id) {
		UserService service = (UserService) userServiceFactory.getObject();
		return service.findById(id);
	}

	public PageInfo<User> findTeachers(int pageIndex, int pageSize) {
		UserService service = (UserService) userServiceFactory.getObject();
		return service.findTeachers(pageIndex, pageSize);
	}
	
	public PageInfo<User> findStudents(int pageIndex, int pageSize, int classId) {
		UserService service = (UserService) userServiceFactory.getObject();
		return service.findStudents(pageIndex, pageSize, classId);
	}

	public void addUsers(List<User> users) {
		UserService service = (UserService) userServiceFactory.getObject();
		service.save(users);
	}

	public void modifyUser(User user) {
		UserService service = (UserService) userServiceFactory.getObject();
		service.update(user);
	}

	public void deleteUser(int id) {
		UserService service = (UserService) userServiceFactory.getObject();
		User user = service.findById(id);
		if (user != null) {
			user.setUpdater(Session.get(Session.KEY_LOGIN_ID));
			user.setDel(true);
			service.update(user);
		}
	}
}

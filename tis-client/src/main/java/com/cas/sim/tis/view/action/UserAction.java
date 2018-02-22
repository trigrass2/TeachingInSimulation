package com.cas.sim.tis.view.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

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

	public PageInfo<User> findUsersByRole(int pageIndex, int pageSize, int role) {
		UserService service = (UserService) userServiceFactory.getObject();
		return service.findUsersByRole(pageIndex, pageSize, role);
	}
	
	public void addUsers(List<User> users) {
		UserService service = (UserService) userServiceFactory.getObject();
		service.save(users);
	}
}

package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;

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
}

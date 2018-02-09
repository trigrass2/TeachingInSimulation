package com.cas.sim.tis.view.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;
import com.github.pagehelper.PageInfo;

@Component
public class ClassAction {
	@Resource
	@Qualifier("classServiceFactory")
	private RmiProxyFactoryBean classServiceFactory;

	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		return service.findClasses(pageIndex, pageSize);
	}

	public void addClasses(List<Class> classes) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		service.addClasses(classes);
	}

	public void deleteClass(int id) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		Class clazz = service.findById(id);
		clazz.setDel(1);
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.modifyClass(clazz);
	}
}

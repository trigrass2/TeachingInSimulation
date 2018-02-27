package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ClassAction {
	@Resource
	@Qualifier("classServiceFactory")
	private RmiProxyFactoryBean classServiceFactory;

	public Class findClass(int id) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		return service.findById(id);
	}

	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		return service.findClasses(pageIndex, pageSize);
	}

	public List<Class> findClassesByTeacher(int teacherId) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		return service.findClassesByTeacher(teacherId);
	}

	public void addClasses(List<ClassInfo> infos) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		service.saveClasses(infos, Session.get(Session.KEY_LOGIN_ID));
	}

	public void modifyClass(Class clazz) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(clazz);
	}

	public void deleteClass(int id) {
		ClassService service = (ClassService) classServiceFactory.getObject();
		Class clazz = service.findById(id);
		clazz.setDel(1);
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(clazz);
	}
}

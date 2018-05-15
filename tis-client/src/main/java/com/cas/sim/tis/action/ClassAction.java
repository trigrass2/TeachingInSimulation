package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ClassAction extends BaseAction {
	@Resource(name = "classService")
	private ClassService service;

	public Class findClass(int id) {
		return service.findById(id);
	}

	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		return service.findClasses(pageIndex, pageSize);
	}

	public List<Class> findClassesByTeacher(int teacherId) {
		return service.findClassesByTeacher(teacherId);
	}

	public void addClasses(List<ClassInfo> infos) {
		service.saveClasses(infos, Session.get(Session.KEY_LOGIN_ID));
	}

	public void modifyClass(Class clazz) {
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.modifyClass(clazz);
	}

	public void deleteClass(int id) {
		Class clazz = service.findById(id);
		clazz.setDel(1);
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(clazz);
	}
}

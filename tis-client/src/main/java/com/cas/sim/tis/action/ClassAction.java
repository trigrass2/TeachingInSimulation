package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ClassAction extends BaseAction {
	@Resource
	private ClassService service;

	public Class findClass(int id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		
		ResponseEntity resp = service.findClassById(req);
		return JSON.parseObject(resp.data, Class.class);
	}

	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		
		ResponseEntity resp = service.findClasses(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Class>>() {});
	}

	public List<Class> findClassesByTeacherId(int teacherId) {
		RequestEntity req = new RequestEntity();
		req.set("teacherId", teacherId).end();
		
		ResponseEntity resp = service.findClassesByTeacherId(req);
		return JSON.parseArray(resp.data, Class.class);
	}

	public void addClasses(List<ClassInfo> infos) {
		RequestEntity req = new RequestEntity();
		req.set("infos", infos).end();
		req.set("creator", Session.get(Session.KEY_LOGIN_ID));
		service.saveClasses(req);
	}

	public void modifyClass(Class clazz) {
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntity();
		req.set("clazz", clazz);
		service.modifyClass(req);
	}

	public void deleteClass(int id) {
		Class clazz = new Class();
		clazz.setDel(1);
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		
		RequestEntity req = new RequestEntity();
		req.set("clazz", clazz);
		service.modifyClass(req);
	}
}

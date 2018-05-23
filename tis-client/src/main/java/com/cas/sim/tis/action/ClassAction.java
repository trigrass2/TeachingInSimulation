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
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ClassAction extends BaseAction {
	@Resource
	private ClassService service;

	/**
	 * 根据ID查询班级信息
	 * @param id 班级编号
	 * @return
	 */
	public Class findClass(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();

		ResponseEntity resp = service.findClassById(req);
		return JSON.parseObject(resp.data, Class.class);
	}

	/**
	 * 分页查询班级信息
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		RequestEntity req = new RequestEntityBuilder()//
				.pageNum(pageIndex)//
				.pageSize(pageSize)//
				.build();

		ResponseEntity resp = service.findClasses(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Class>>() {});
	}

	/**
	 * 根据教师编号查询班级集合
	 * @param teacherId 教师编号
	 * @return
	 */
	public List<Class> findClassesByTeacherId(int teacherId) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("teacherId", teacherId)//
				.build();

		ResponseEntity resp = service.findClassesByTeacherId(req);
		return JSON.parseArray(resp.data, Class.class);
	}

	public void addClass(Class clazz) {
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("clazz", clazz)//
				.build();
		service.addClass(req);
	}

	/**
	 * 批量导入班级信息
	 * @param infos 班级信息集合
	 */
	public void addClasses(List<ClassInfo> infos) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("infos", infos)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.saveClasses(req);
	}

	/**
	 * 修改班级信息
	 * @param clazz 班级信息对象
	 */
	public void modifyClass(Class clazz) {
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("clazz", clazz)//
				.build();
		service.modifyClass(req);
	}

	/**
	 * 逻辑删除班级信息
	 * @param id 班级编号
	 */
	public void deleteClassByLogic(int id) {
		Class clazz = new Class();
		clazz.setId(id);
		clazz.setDel(1);
		clazz.setUpdater(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntityBuilder()//
				.set("clazz", clazz)//
				.build();
		service.modifyClass(req);
	}
}

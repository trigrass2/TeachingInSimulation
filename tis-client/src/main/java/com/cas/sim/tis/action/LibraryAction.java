package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryAction extends BaseAction {
	@Resource
	private LibraryService service;

	/**
	 * 按条件查询题库信息集合
	 * @param type 题库类型
	 * @param pageNum 当前查询页数
	 * @param pageSize 当前查询条数
	 * @return 返回分页集合(id, name, time, num, createDate)
	 */
	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("type", type)//
				.build();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		ResponseEntity resp = service.findLibraryByType(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Library>>() {});
	}

	/**
	 * 按条件查询题库信息集合
	 * @param type 题库类型
	 * @param keyword 关键词查询
	 * @return 返回题库信息集合(id, name, time, num, createDate)
	 */
	public List<Library> findLibraryByType(int type, String keyword) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("type", type)//
				.set("keyword", keyword)//
				.build();
		ResponseEntity resp = service.findLibraryByType(req);
		return JSON.parseArray(resp.data, Library.class);
	}

	/**
	 * 根据试题库编号查询试题库信息对象
	 * @param id 试题库编号
	 * @return 试题库信息对象
	 */
	public Library findLibraryById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findLibraryById(req);
		return JSON.parseObject(resp.data, Library.class);
	}

	/**
	 * 新增题库信息
	 * @param library 试题库信息对象
	 */
	public void addLibrary(Library library) {
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("library", library)//
				.build();
		service.savelibrary(req);
	}

	/**
	 * 修改题库信息
	 * @param library 试题库信息对象
	 */
	public void modifyLibrary(Library library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("library", library)//
				.build();
		service.updatelibrary(req);
	}

	/**
	 * 删除题库信息（逻辑删除）
	 * @param library 试题库信息对象
	 */
	public void deleteLibraryByLogic(int id) {
		Library library = new Library();
		library.setId(id);
		library.setDel(1);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntityBuilder()//
				.set("library", library)//
				.build();
		service.updatelibrary(req);
	}
}

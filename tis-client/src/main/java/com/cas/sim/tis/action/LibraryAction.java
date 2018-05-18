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
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryAction extends BaseAction {
	@Resource
	private LibraryService service;

	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		req.set("type", type).end();
		ResponseEntity resp = service.findLibraryByType(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Library>>() {});
	}

	public List<Library> findLibraryByType(int type, String key) {
		RequestEntity req = new RequestEntity();
		req.set("type", type).set("key", key).end();
		ResponseEntity resp = service.findLibraryByType(req);
		return JSON.parseArray(resp.data, Library.class);
	}

	public Library findLibraryById(int id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		ResponseEntity resp = service.findLibraryById(req);
		return JSON.parseObject(resp.data, Library.class);
	}

	public void addLibrary(Library library) {
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntity();
		req.set("library", library).end();
		service.savelibrary(req);
	}

	public void modifyLibrary(Library library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntity();
		req.set("library", library).end();
		service.updatelibrary(req);
	}

	public void deleteLibrary(int id) {
		Library library = new Library();
		library.setId(id);
		library.setDel(1);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		RequestEntity req = new RequestEntity();
		req.set("library", library).end();
		service.updatelibrary(req);
	}
}

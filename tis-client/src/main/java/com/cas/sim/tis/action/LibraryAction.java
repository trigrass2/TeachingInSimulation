package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.services.LibraryService;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryAction extends BaseAction {
	@Resource
	private LibraryService service;

	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		return service.findLibraryByType(pageIndex, pageSize, type);
	}

	public List<Library> findLibraryByType(int type, String key) {
		return service.findLibraryByType(type, key);
	}

	public Library findLibraryByID(int id) {
		return service.findById(id);
	}

	public void addLibrary(Library library) {
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(library);
	}

	public void modifyLibrary(Library library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(library);
	}

	public void deleteLibrary(int id) {
		Library library = service.findById(id);
		library.setDel(1);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(library);
	}
}

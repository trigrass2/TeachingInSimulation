package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.services.LibraryService;
import com.github.pagehelper.PageInfo;
@Component
public class LibraryAction {
	@Resource
	@Qualifier("libraryServiceFactory")
	private RmiProxyFactoryBean libraryServiceFactory;

	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		LibraryService service = (LibraryService) libraryServiceFactory.getObject();
		return service.findLibraryByType(pageIndex, pageSize, type);
	}
	
	public Library findLibraryByID(int id) {
		LibraryService service = (LibraryService) libraryServiceFactory.getObject();
		return service.findById(id);
	}
	
	public void addLibrary(Library library) {
		LibraryService service = (LibraryService) libraryServiceFactory.getObject();
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.addLibrary(library);
	}
	
	public void modifyLibrary(int id, String name) {
		LibraryService service = (LibraryService) libraryServiceFactory.getObject();
		Library library = service.findById(id);
		library.setName(name);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.modifyLibrary(library);
	}
	
	public void deleteLibrary(int id) {
		LibraryService service = (LibraryService) libraryServiceFactory.getObject();
		Library library = service.findById(id);
		library.setDel(1);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.modifyLibrary(library);
	}
	
}

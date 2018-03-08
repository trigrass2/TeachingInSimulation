package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.services.LibraryService;
import com.github.pagehelper.PageInfo;

@Component
public class LibraryAction extends BaseAction<LibraryService> {
	@Resource
	@Qualifier("libraryServiceFactory")
	private RmiProxyFactoryBean libraryServiceFactory;

	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		return getService().findLibraryByType(pageIndex, pageSize, type);
	}

	public List<Library> findLibraryByType(int type, String key) {
		return getService().findLibraryByType(type, key);
	}

	public Library findLibraryByID(int id) {
		return getService().findById(id);
	}

	public void addLibrary(Library library) {
		library.setCreator(Session.get(Session.KEY_LOGIN_ID));
		getService().save(library);
	}

	public void modifyLibrary(Library library) {
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		getService().update(library);
	}

	public void deleteLibrary(int id) {
		LibraryService service = getService();
		Library library = service.findById(id);
		library.setDel(1);
		library.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(library);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return libraryServiceFactory;
	}
}

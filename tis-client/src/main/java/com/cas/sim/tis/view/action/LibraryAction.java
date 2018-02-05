package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

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
	
	
}

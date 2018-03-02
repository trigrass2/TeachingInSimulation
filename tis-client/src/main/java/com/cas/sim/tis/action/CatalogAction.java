package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.services.CatalogService;

@Component
public class CatalogAction {
	@Resource
	@Qualifier("catalogServiceFactory")
	private RmiProxyFactoryBean catalogServiceFactory;

	public List<Catalog> findCatalogsByParentId(Integer id) {
		CatalogService service = (CatalogService) catalogServiceFactory.getObject();
		return service.findCatalogsByParentId(id);
	}

}

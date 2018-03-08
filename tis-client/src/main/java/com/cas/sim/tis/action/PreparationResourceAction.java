package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationResourceAction extends BaseAction<PreparationResourceService> {
	@Resource
	@Qualifier("preparationResourceServiceFactory")
	private RmiProxyFactoryBean preparationResourceServiceFactory;

	public List<PreparationInfo> findResourcesByPreparationId(Integer pid) {
		PreparationResourceService service = getService();
		return service.findResourcesByPreparationId(pid);
	}

	public PreparationResource findResourceById(Integer id) {
		PreparationResourceService service = getService();
		return service.findById(id);
	}
	
	public void detele(Integer rid) {
		PreparationResourceService service = getService();
		PreparationResource resource = service.findById(rid);
		resource.setDel(true);
		resource.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(resource);
	}

	public void addResource(PreparationResource resource) {
		PreparationResourceService service = getService();
		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(resource);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return preparationResourceServiceFactory;
	}
}

package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.CollectionService;

@Component
public class CollectionAction {
	@Resource
	@Qualifier("collectionServiceFactory")
	private RmiProxyFactoryBean collectionServiceFactory;

	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		CollectionService service = (CollectionService) collectionServiceFactory.getObject();
		return service.checkCollected(rid);
	}
}

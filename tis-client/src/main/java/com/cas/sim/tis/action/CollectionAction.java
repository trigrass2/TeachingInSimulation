package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.CollectionService;

@Component
public class CollectionAction extends BaseAction<CollectionService> {
	@Resource
	@Qualifier("collectionServiceFactory")
	private RmiProxyFactoryBean collectionServiceFactory;

	public boolean checkCollected(Integer rid) {
		if (rid == null) {
			return false;
		}
		return getService().checkCollected(rid);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return collectionServiceFactory;
	}
}

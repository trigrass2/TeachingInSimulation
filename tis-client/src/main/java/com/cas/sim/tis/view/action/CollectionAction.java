package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Collection;
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

	public void uncollect(Integer rid) {
		if (rid == null) {
			return;
		}
		CollectionService service = (CollectionService) collectionServiceFactory.getObject();
		service.uncollect(rid);
	}

	public void collected(Integer rid) {
		if (rid == null) {
			return;
		}
		Collection collection = new Collection();
		collection.setResourceId(rid);
		collection.setCreator(Session.get(Session.KEY_LOGIN_ID));
		
		CollectionService service = (CollectionService) collectionServiceFactory.getObject();
		service.collected(collection);
	}
}

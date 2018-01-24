package com.cas.sim.tis.view.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.services.CollectionService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

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

		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("RID", rid);

		int total = service.getTotalBy(condition);
		if (total == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void uncollect(Integer rid) {
		if (rid == null) {
			return;
		}
		CollectionService service = (CollectionService) collectionServiceFactory.getObject();

		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("RID", rid);
		criteria.andEqualTo("DEL", 0);

		List<Collection> collections = service.findByCondition(condition);
		for (Collection collection : collections) {
			collection.setDel(1);
			service.update(collection);
		}
	}

	public void collected(Integer rid) {
		if (rid == null) {
			return;
		}
		CollectionService service = (CollectionService) collectionServiceFactory.getObject();

		Collection collection = new Collection();
		collection.setResourceId(rid);
		collection.setCreator(Session.get(Session.KEY_LOGIN_ID));
		collection.setCreateDate(new Date());

		service.save(collection);
	}
}

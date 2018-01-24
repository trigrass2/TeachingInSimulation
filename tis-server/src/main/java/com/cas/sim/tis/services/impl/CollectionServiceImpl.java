package com.cas.sim.tis.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.services.CollectionService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("collectionService")
public class CollectionServiceImpl extends AbstractService<Collection> implements CollectionService {

	@Override
	public boolean checkCollected(Integer rid) {
		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("RID", rid);

		int total = getTotalBy(condition);
		if (total == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void uncollect(Integer rid) {
		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("RID", rid);
		criteria.andEqualTo("DEL", 0);

		List<Collection> collections = findByCondition(condition);
		for (Collection collection : collections) {
			collection.setDel(1);
			update(collection);
		}
	}

	@Override
	public void collected(Collection collection) {
		collection.setCreateDate(new Date());
		save(collection);
	}

}

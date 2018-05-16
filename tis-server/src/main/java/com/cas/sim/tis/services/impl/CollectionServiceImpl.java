package com.cas.sim.tis.services.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.services.CollectionService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CollectionServiceImpl extends AbstractService<Collection> implements CollectionService {
	
	@Override
	public boolean checkCollected(Integer rid) {
		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("resourceId", rid);

		int total = getTotalBy(condition);
		if (total == 0) {
			return false;
		} else {
			return true;
		}
	}

}

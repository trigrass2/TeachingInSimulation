package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.mapper.CollectionMapper;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CollectionServiceImpl implements CollectionService {
	
	@Resource
	private CollectionMapper mapper;
	
	@Override
	public ResponseEntity checkCollected(RequestEntity entity) {
		int rid = entity.getInt("rid");
		int creator = entity.getInt("creator");
		
		Condition condition = new Condition(Collection.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("resourceId", rid);
		criteria.andEqualTo("creator", creator);
		criteria.andEqualTo("del", 0);
		
		int total = mapper.selectCountByCondition(condition);
		if (total == 0) {
			return ResponseEntity.success(false);
		} else {
			return ResponseEntity.success(true);
		}
	}
}

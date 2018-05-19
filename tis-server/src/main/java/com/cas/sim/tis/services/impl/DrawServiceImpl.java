package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.mapper.DrawMapper;
import com.cas.sim.tis.services.DrawService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class DrawServiceImpl implements DrawService {

	@Resource
	private DrawMapper mapper;

	@Override
	public ResponseEntity findDraws(RequestEntity entity) {
		Integer creatorId = entity.getObject("creator", Integer.class);

		Condition condition = new Condition(Draw.class);
		condition.selectProperties("id", "name", "paths", "createDate");

		Criteria criteria = condition.createCriteria();
		if (creatorId != null) {
			criteria.andEqualTo("creatorId", creatorId);
		}
		criteria.andEqualTo("del", 0);

		List<Draw> draws = mapper.selectByCondition(condition);
		return ResponseEntity.success(draws);
	}

}

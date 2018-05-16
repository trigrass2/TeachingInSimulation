package com.cas.sim.tis.services.impl;

import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.mapper.DrawMapper;
import com.cas.sim.tis.services.DrawService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class DrawServiceImpl extends AbstractService<Draw> implements DrawService {

	@Override
	public List<Draw> findByCreatorId(int creatorId) {
		DrawMapper mapper = (DrawMapper) this.mapper;
		Condition condition = new Condition(Draw.class);

		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("creatorId", creatorId);
		criteria.andEqualTo("del", 0);
		return mapper.selectByCondition(condition);
	}

}

package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Draw;
import com.cas.sim.tis.mapper.DrawMapper;
import com.cas.sim.tis.services.DrawService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class DrawServiceImpl extends AbstractService<Draw> implements DrawService {

	@Override
	public List<Draw> findBySystem() {
		DrawMapper dao = (DrawMapper) mapper;
		Condition condition = new Condition(Draw.class);

		Criteria criteria = condition.createCriteria();
//		FIXME 系统
		criteria.andEqualTo("creatorId", 0);
		return dao.selectByCondition(condition);
	}

	@Override
	public List<Draw> findByMine(int mineId) {
		DrawMapper dao = (DrawMapper) mapper;
		Condition condition = new Condition(Draw.class);

		Criteria criteria = condition.createCriteria();
//		FIXME 系统
		criteria.andEqualTo("creatorId", mineId);
		return dao.selectByCondition(condition);
	}

}

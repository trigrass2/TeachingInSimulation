package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.consts.PreparationQuizType;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.mapper.PreparationQuizMapper;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.vo.PreparationInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class PreparationQuizServiceImpl extends AbstractService<PreparationQuiz> implements PreparationQuizService {

	@Override
	public List<PreparationInfo> findQuizsByPreparationId(Integer pid) {
		PreparationQuizMapper mapper = (PreparationQuizMapper) this.mapper;
		List<PreparationInfo> tests = mapper.findQuizsByPreparationId(pid);
		if (tests == null) {
			return new ArrayList<>();
		} else {
			return tests;
		}
	}

	@Override
	public int countFreeQuizByPreparationId(Integer pid) {
		Condition condition = new Condition(PreparationQuiz.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("preparationId", pid);
		criteria.andEqualTo("type", PreparationQuizType.FREE.getType());
		criteria.andEqualTo("del", 0);
		return getTotalBy(condition);
	}

}

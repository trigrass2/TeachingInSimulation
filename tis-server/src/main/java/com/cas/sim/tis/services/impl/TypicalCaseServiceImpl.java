package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.services.TypicalCaseService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class TypicalCaseServiceImpl extends AbstractService<TypicalCase> implements TypicalCaseService {

	@Override
	public List<TypicalCase> findTypicalCases() {
		Condition condition = new Condition(TypicalCase.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("del", 0);
		return findByCondition(condition);
	}}

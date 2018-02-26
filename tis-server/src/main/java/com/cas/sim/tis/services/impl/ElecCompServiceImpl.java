package com.cas.sim.tis.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.mapper.ElecCompMapper;
import com.cas.sim.tis.services.ElecCompService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ElecCompServiceImpl extends AbstractService<ElecComp> implements ElecCompService {

	@Override
	public Map<String, List<ElecComp>> findElecCompGroupByType() {
		ElecCompMapper dao = (ElecCompMapper) mapper;
		List<ElecComp> all = dao.selectAll();

		if (all == null) {
			return new HashMap<>();
		}

		return all.stream().collect(Collectors.groupingBy(ElecComp::getType));
	}

	@Override
	public ElecComp findElecCompByModel(String model) {
		ElecCompMapper dao = (ElecCompMapper) mapper;
		Condition condition = new Condition(ElecComp.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("model", model);
		List<ElecComp> compList = dao.selectByCondition(condition);
		if (compList.size() == 0) {
			return null;
		} else if (compList.size() > 1) {
			throw new TooManyResultsException();
		}
		return compList.get(0);
	}
}

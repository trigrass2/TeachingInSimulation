package com.cas.sim.tis.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.mapper.ElecCompMapper;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ElecCompServiceImpl implements ElecCompService {

	@Resource
	private ElecCompMapper mapper;

	@Override
	public ResponseEntity findElecCompGroupByType() {
		List<ElecComp> all = mapper.selectAll();

		if (all == null) {
			return ResponseEntity.success(new HashMap<>());
		}

		Map<Integer, List<ElecComp>> map = all.stream().collect(Collectors.groupingBy(ElecComp::getType));

		return ResponseEntity.success(map);
	}

	@Override
	public ResponseEntity findElecCompsByRecongnize() {
		Condition condition = new Condition(ElecComp.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("recongnize", true);
		List<ElecComp> compList = mapper.selectByCondition(condition);
		return ResponseEntity.success(compList);
	}

	@Override
	public ResponseEntity findElecCompById(RequestEntity req) {
		int id = req.getInt("id");
		return ResponseEntity.success(mapper.selectByPrimaryKey(id));
	}

	@Override
	public ResponseEntity findElecCompByModel(RequestEntity entity) {
		String model = entity.getString("model");

		Condition condition = new Condition(ElecComp.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("model", model);
		List<ElecComp> compList = mapper.selectByCondition(condition);
		if (compList.size() == 0) {
			return ResponseEntity.success(null);
		} else if (compList.size() > 1) {
			throw new TooManyResultsException();
		}
		return ResponseEntity.success(compList.get(0));
	}

	@Override
	public ResponseEntity findElecComps() {
		return ResponseEntity.success(mapper.selectAll());
	}
}

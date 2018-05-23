package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.mapper.PreparationMapper;
import com.cas.sim.tis.services.PreparationService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;

@Service
public class PreparationServiceImpl implements PreparationService {
	@Resource
	private PreparationMapper mapper;

	@Override
	public ResponseEntity findPreparationByTaskIdAndCreator(RequestEntity entity) {
		Condition condition = new Condition(Preparation.class);
		condition.createCriteria()//
				.andEqualTo("catalogId", entity.getInt("cid"))//
				.andEqualTo("creator", entity.getInt("creator"))//
				.andEqualTo("del", 0);

		Preparation preparation = mapper.selectOneByExample(condition);
		return ResponseEntity.success(preparation);
	}

	@Override
	public ResponseEntity addPreparation(RequestEntity entity) {
		Preparation preparation = entity.getObject("preparation", Preparation.class);
		mapper.insert(preparation);
		return ResponseEntity.success(preparation);
	}

}

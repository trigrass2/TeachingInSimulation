package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.TypicalCase;
import com.cas.sim.tis.mapper.TypicalCaseMapper;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;

@Service
public class TypicalCaseServiceImpl implements TypicalCaseService {
	@Resource
	private TypicalCaseMapper mapper;

	@Override
	public ResponseEntity findTypicalCasesByCreatorId(RequestEntity entity) {
		Condition condition = new Condition(TypicalCase.class);
		if (entity.getBoolean("onlyPublished")) {
			condition.createCriteria()//
					.andEqualTo("del", 0)//
					.andEqualTo("creator", entity.getInt("creator"))//
					.andEqualTo("publish", true);
		} else {
			condition.createCriteria()//
					.andEqualTo("del", 0)//
					.andEqualTo("creator", entity.getInt("creator"));
		}
		condition.orderBy("createDate").desc();
		List<TypicalCase> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity saveTypicalCase(RequestEntity entity) {
		TypicalCase typicalCase = entity.getObject("typicalCase", TypicalCase.class);
		mapper.insertUseGeneratedKeys(typicalCase);
		return ResponseEntity.success(typicalCase);
	}

	@Override
	public ResponseEntity findTypicalCasesById(RequestEntity entity) {
		Condition condition = new Condition(TypicalCase.class);
		condition.createCriteria()//
				.andEqualTo("del", 0)//
				.andEqualTo("id", entity.getInt("id"));
		TypicalCase result = mapper.selectOneByExample(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public void updateTypicalCase(RequestEntity entity) {
		TypicalCase typicalCase = entity.getObject("typicalCase", TypicalCase.class);
		mapper.updateByPrimaryKeySelective(typicalCase);
	}
}

package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.BrokenCase;
import com.cas.sim.tis.mapper.BrokenCaseMapper;
import com.cas.sim.tis.services.BrokenCaseService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;

@Service
public class BrokenCaseServiceImpl implements BrokenCaseService {

	@Resource
	private BrokenCaseMapper mapper;

	@Override
	public ResponseEntity findBrokenCases() {
		return ResponseEntity.success(mapper.selectAll());
	}

	@Override
	public ResponseEntity findBrokenCasesById(RequestEntity entity) {
		Condition condition = new Condition(BrokenCase.class);
		condition.createCriteria()//
				.andEqualTo("del", 0)//
				.andEqualTo("id", entity.getInt("id"));
		BrokenCase result = mapper.selectOneByExample(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findBrokenCaseByCreatorId(RequestEntity entity) {
		Condition condition = new Condition(BrokenCase.class);
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
		List<BrokenCase> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity saveBrokenCase(RequestEntity entity) {
		BrokenCase brokenCase = entity.getObject("brokenCase", BrokenCase.class);
		mapper.insertUseGeneratedKeys(brokenCase);
		return ResponseEntity.success(brokenCase);
	}

	@Override
	public void updateBrokenCase(RequestEntity entity) {
		BrokenCase brokenCase = entity.getObject("brokenCase", BrokenCase.class);
		mapper.updateByPrimaryKeySelective(brokenCase);
	}
}

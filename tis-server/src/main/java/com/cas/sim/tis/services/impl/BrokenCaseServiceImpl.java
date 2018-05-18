package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.cas.sim.tis.mapper.BrokenCaseMapper;
import com.cas.sim.tis.services.BrokenCaseService;
import com.cas.sim.tis.thrift.ResponseEntity;

@Service
public class BrokenCaseServiceImpl implements BrokenCaseService {

	@Resource
	private BrokenCaseMapper mapper;

	@Override
	public ResponseEntity findBrokenCases() {
		return ResponseEntity.success(mapper.selectAll());
	}
}

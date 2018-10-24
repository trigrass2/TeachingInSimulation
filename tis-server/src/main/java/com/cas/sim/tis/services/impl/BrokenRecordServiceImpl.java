package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.BrokenRecord;
import com.cas.sim.tis.mapper.BrokenRecordMapper;
import com.cas.sim.tis.services.BrokenRecordService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
@Service
public class BrokenRecordServiceImpl implements BrokenRecordService {

	@Resource
	private BrokenRecordMapper mapper;
	
	@Override
	public ResponseEntity saveBrokenRecord(RequestEntity entity) {
		BrokenRecord record = entity.getObject("record", BrokenRecord.class);
		mapper.insertUseGeneratedKeys(record);
		return ResponseEntity.success(record);
	}

}

package com.cas.sim.tis.services.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.ExamBrokenRecord;
import com.cas.sim.tis.entity.ExamLibraryRecord;
import com.cas.sim.tis.mapper.ExamBrokenRecordMapper;
import com.cas.sim.tis.services.ExamBrokenRecordService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class ExamBrokenRecordServiceImpl implements ExamBrokenRecordService {

	@Resource
	private ExamBrokenRecordMapper mapper;
	
	@Override
	public ResponseEntity saveBrokenRecord(RequestEntity entity) {
		ExamBrokenRecord record = entity.getObject("record", ExamBrokenRecord.class);
		mapper.insertUseGeneratedKeys(record);
		return ResponseEntity.success(record);
	}

}

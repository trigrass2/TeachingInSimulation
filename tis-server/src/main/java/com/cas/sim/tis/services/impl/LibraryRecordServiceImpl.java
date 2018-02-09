package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.mapper.LibraryRecordMapper;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.vo.LibraryRecordInfo;

@Service
public class LibraryRecordServiceImpl extends AbstractService<LibraryRecord> implements LibraryRecordService {

	@Override
	public List<LibraryRecordInfo> findRecordByPublishId(int pid) {
		LibraryRecordMapper recordMapper = (LibraryRecordMapper) mapper;
		return recordMapper.findRecordByPublishId(pid);
	}

}

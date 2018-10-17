package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.ArchiveCase;
import com.cas.sim.tis.mapper.ArchiveCaseMapper;
import com.cas.sim.tis.services.ArchiveCaseService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;

@Service
public class ArchiveCaseServiceImpl implements ArchiveCaseService {
	@Resource
	private ArchiveCaseMapper mapper;

	@Override
	public ResponseEntity findArchiveCasesByCreatorId(RequestEntity entity) {
		Condition condition = new Condition(ArchiveCase.class);
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
		List<ArchiveCase> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity saveArchiveCase(RequestEntity entity) {
		ArchiveCase archiveCase = entity.getObject("archiveCase", ArchiveCase.class);
		mapper.insertUseGeneratedKeys(archiveCase);
		return ResponseEntity.success(archiveCase);
	}

	@Override
	public ResponseEntity findArchiveCasesById(RequestEntity entity) {
		Condition condition = new Condition(ArchiveCase.class);
		condition.createCriteria()//
				.andEqualTo("del", 0)//
				.andEqualTo("id", entity.getInt("id"));
		ArchiveCase result = mapper.selectOneByExample(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public void updateArchiveCase(RequestEntity entity) {
		ArchiveCase archiveCase = entity.getObject("archiveCase", ArchiveCase.class);
		mapper.updateByPrimaryKeySelective(archiveCase);
	}
}

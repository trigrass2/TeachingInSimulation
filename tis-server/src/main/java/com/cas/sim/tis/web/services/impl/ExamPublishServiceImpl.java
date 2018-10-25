package com.cas.sim.tis.web.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.entity.ExamBrokenRecord;
import com.cas.sim.tis.entity.ExamPublish;
import com.cas.sim.tis.entity.ExamLibraryRecord;
import com.cas.sim.tis.mapper.ExamBrokenPublishMapper;
import com.cas.sim.tis.mapper.ExamBrokenRecordMapper;
import com.cas.sim.tis.mapper.ExamPublishMapper;
import com.cas.sim.tis.mapper.ExamLibraryPublishMapper;
import com.cas.sim.tis.mapper.ExamLibraryRecordMapper;
import com.cas.sim.tis.mapper.ExamPreparationPublishMapper;
import com.cas.sim.tis.web.services.ExamPublishService;

import tk.mybatis.mapper.entity.Condition;

@Service
public class ExamPublishServiceImpl extends IWebServiceImpl<ExamPublish> implements ExamPublishService {

	@Resource
	private ExamPublishMapper mapper;
	
	@Resource
	private ExamLibraryPublishMapper libraryPublishMapper;
	@Resource
	private ExamLibraryRecordMapper libraryRecordMapper;
	
	@Resource
	private ExamPreparationPublishMapper preparationPublishMapper;
	
	@Resource
	private ExamBrokenPublishMapper brokenPublishMapper;
	@Resource
	private ExamBrokenRecordMapper brokenRecordMapper;
	
	
	@Override
	public ExamPublish findExamingByCreator(int creator) {
		Condition condition = new Condition(ExamPublish.class);
		condition.createCriteria()
				.andEqualTo("creator", creator)
				.andEqualTo("state", false);//
		List<ExamPublish> publishs = mapper.selectByCondition(condition);
		if (publishs.size() == 0) {
			return null;
		} else if (publishs.size() > 1) {
			throw new TooManyResultsException();
		}
		return publishs.get(0);
	}

	@Override
	public ExamPublish findExamingByClassId(int classId) {
		Condition condition = new Condition(ExamPublish.class);
		condition.createCriteria()
				.andEqualTo("classId", classId)
				.andEqualTo("state", false);
		List<ExamPublish> publishs = mapper.selectByCondition(condition);
		if (publishs.size() == 0) {
			return null;
		} else if (publishs.size() > 1) {
			throw new TooManyResultsException();
		}
		return publishs.get(0);
	}


	@Override
	public int conutLibraryRecordByPidAndSid(Integer publishId, Integer studentId, LibraryRecordType type) {
		Condition condition = new Condition(ExamLibraryRecord.class);
		condition.createCriteria()//
				.andEqualTo("publishId", publishId)//
				.andEqualTo("creator", studentId)//
				.andEqualTo("type", type.getType());
		return libraryRecordMapper.selectCountByCondition(condition);
	}
	
	@Override
	public int conutBrokenRecordByPidAndSid(Integer publishId, Integer studentId) {
		Condition condition = new Condition(ExamBrokenRecord.class);
		condition.createCriteria()//
				.andEqualTo("publishId", publishId)//
				.andEqualTo("creator", studentId);
		return brokenRecordMapper.selectCountByCondition(condition);
	}

}

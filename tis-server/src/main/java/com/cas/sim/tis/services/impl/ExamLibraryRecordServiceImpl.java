package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.entity.ExamLibraryRecord;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.ExamLibraryAnswerMapper;
import com.cas.sim.tis.mapper.ExamLibraryPublishMapper;
import com.cas.sim.tis.mapper.ExamLibraryRecordMapper;
import com.cas.sim.tis.mapper.ExamPreparationPublishMapper;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.services.ExamLibraryRecordService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.vo.ExamLibraryPublish;
import com.cas.util.MathUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class ExamLibraryRecordServiceImpl implements ExamLibraryRecordService {

	@Resource
	private ExamLibraryRecordMapper mapper;

	@Resource
	private UserMapper userMapper;
	@Resource
	private ExamLibraryPublishMapper publishMapper;
	@Resource
	private ExamLibraryAnswerMapper answerMapper;
	@Resource
	private ExamPreparationPublishMapper preparationPublishMapper;

	@Override
	@Transactional
	public void addRecord(RequestEntity entity) {
		ExamLibraryRecord record = entity.getObject("record", ExamLibraryRecord.class);
		mapper.insertUseGeneratedKeys(record);

		int recordId = record.getId();

		List<ExamLibraryAnswer> answers = entity.getList("answers", ExamLibraryAnswer.class);

		for (ExamLibraryAnswer answer : answers) {
			answer.setRecordId(recordId);
		}
		answerMapper.insertList(answers);

		int type = record.getType();
		if (LibraryRecordType.LIBRARY.getType() == type) {
			ExamLibraryPublish publish = publishMapper.selectByPrimaryKey(record.getPublishId());

			// 获得班级总人数
			if (publish.getClassId() == null) {
				publish.setAverage(MathUtil.round(2, record.getScore()));
			} else {
				Condition userCon = new Condition(User.class);
				Criteria criteria = userCon.createCriteria();
				criteria.andEqualTo("classId", publish.getClassId());
				criteria.andEqualTo("del", 0);
				int total = userMapper.selectCountByCondition(userCon);
				// 获得已交卷成绩总和
				ExamLibraryRecordMapper recordMapper = (ExamLibraryRecordMapper) mapper;
				float sum = recordMapper.getRecordScoresSumByPublishId(publish.getId(), LibraryRecordType.LIBRARY.getType());
				publish.setAverage(MathUtil.round(2, sum / total));
			}
			// 更新平均成绩
			publishMapper.updateByPrimaryKeySelective(publish);
		}
	}
}

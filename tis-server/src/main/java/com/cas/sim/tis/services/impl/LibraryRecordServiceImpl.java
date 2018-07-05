package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.LibraryAnswerMapper;
import com.cas.sim.tis.mapper.LibraryPublishMapper;
import com.cas.sim.tis.mapper.LibraryRecordMapper;
import com.cas.sim.tis.mapper.PreparationPublishMapper;
import com.cas.sim.tis.mapper.UserMapper;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.util.MathUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class LibraryRecordServiceImpl implements LibraryRecordService {

	@Resource
	private LibraryRecordMapper mapper;

	@Resource
	private UserMapper userMapper;
	@Resource
	private LibraryPublishMapper publishMapper;
	@Resource
	private LibraryAnswerMapper answerMapper;
	@Resource
	private PreparationPublishMapper preparationPublishMapper;

	@Override
	@Transactional
	public void addRecord(RequestEntity entity) {
		LibraryRecord record = entity.getObject("record", LibraryRecord.class);
		mapper.insertUseGeneratedKeys(record);

		int recordId = record.getId();

		List<LibraryAnswer> answers = entity.getList("answers", LibraryAnswer.class);

		for (LibraryAnswer answer : answers) {
			answer.setRecordId(recordId);
		}
		answerMapper.insertList(answers);

		int type = record.getType();
		if (LibraryRecordType.LIBRARY.getType() == type) {
			LibraryPublish publish = publishMapper.selectByPrimaryKey(record.getPublishId());

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
				LibraryRecordMapper recordMapper = (LibraryRecordMapper) mapper;
				float sum = recordMapper.getRecordScoresSumByPublishId(publish.getId(), LibraryRecordType.LIBRARY.getType());
				publish.setAverage(MathUtil.round(2, sum / total));
			}
			// 更新平均成绩
			publishMapper.updateByPrimaryKeySelective(publish);
		}
	}

}

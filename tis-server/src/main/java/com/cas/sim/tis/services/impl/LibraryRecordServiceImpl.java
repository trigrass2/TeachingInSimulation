package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.entity.LibraryRecord;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.mapper.LibraryRecordMapper;
import com.cas.sim.tis.services.LibraryAnswerService;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.SpringUtil;
import com.cas.sim.tis.vo.LibraryRecordInfo;
import com.cas.util.MathUtil;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class LibraryRecordServiceImpl implements LibraryRecordService {

	private static final Logger LOG = LoggerFactory.getLogger(LibraryRecordServiceImpl.class);

	@Resource
	private LibraryRecordMapper mapper;

	@Resource
	private UserService userService;
	@Resource
	private LibraryPublishService publishService;
	@Resource
	private LibraryAnswerService answerService;

	@Override
	public ResponseEntity findRecordByPublishId(RequestEntity entity) {
		List<LibraryRecordInfo> result = mapper.findRecordByPublishId(entity.getInt("pid"));
		return ResponseEntity.success(result);
	}

	@Override
	public void addRecord(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			LibraryRecord record = entity.getObject("record", LibraryRecord.class);
			mapper.insert(record);

			int recordId = record.getId();

			List<LibraryAnswer> answers = entity.getList("entity", LibraryAnswer.class);

			for (LibraryAnswer answer : answers) {
				answer.setRecordId(recordId);
			}
			answerService.save(answers);

			LibraryPublish publish = publishService.findById(record.getPublishId());

			// 获得班级总人数
			if (publish.getClassId() == null) {
				publish.setAverage(MathUtil.round(2, record.getScore()));
			} else {
				Condition userCon = new Condition(User.class);
				Criteria criteria = userCon.createCriteria();
				criteria.andEqualTo("classId", publish.getClassId());
				criteria.andEqualTo("del", 0);
				int total = userService.getTotalBy(userCon);
				// 获得已交卷成绩总和
				LibraryRecordMapper recordMapper = (LibraryRecordMapper) mapper;
				float sum = recordMapper.getRecordScoresSumByPublishId(publish.getId());
				publish.setAverage(MathUtil.round(2, sum / total));
			}
			// 更新平均成绩
			publishService.update(publish);

			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

}

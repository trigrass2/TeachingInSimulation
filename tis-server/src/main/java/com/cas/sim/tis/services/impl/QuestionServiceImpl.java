package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.LibraryRecordType;
import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.PreparationPublish;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.mapper.PreparationPublishMapper;
import com.cas.sim.tis.mapper.QuestionMapper;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.SpringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

	@Resource
	private QuestionMapper mapper;

	@Resource
	private LibraryMapper libraryMapper;
	@Resource
	private PreparationPublishMapper preparationPublishMapper;

	@Override
	public ResponseEntity findQuestionsByLibrary(RequestEntity entity) {
		Condition condition = new Condition(Question.class);
		condition.createCriteria()//
				.andEqualTo("relateId", entity.getInt("rid"));
		condition.orderBy("type").asc();

		if (entity.pageNum != -1) {
			PageHelper.startPage(entity.pageNum, entity.pageSize);
		}

		List<Question> result = mapper.selectByCondition(condition);

		if (entity.pageNum != -1) {
			PageInfo<Question> page = new PageInfo<>(result);
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findQuestionsByLibraryAndQuestionType(RequestEntity entity) {
		Condition condition = new Condition(Question.class);
		condition.createCriteria()//
				.andEqualTo("relateId", entity.getInt("rid"))//
				.andEqualTo("type", entity.getInt("type"));
		List<Question> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findQuestionsByPublishId(RequestEntity entity) {
		int pid = entity.getInt("pid");
		int type = entity.getInt("type");
		boolean mostWrong = entity.getBoolean("mostWrong");
		if (LibraryRecordType.LIBRARY.getType() == type) {
			return ResponseEntity.success(mapper.findQuestionsByLibraryPublish(pid, mostWrong));
		} else if (LibraryRecordType.PREPARATION.getType() == type) {
			PreparationPublish publish = preparationPublishMapper.findPublishById(pid);
			String questionIdsStr = publish.getLibrary().getQuestionIds();
			if (StringUtils.isEmpty(questionIdsStr)) {
				return ResponseEntity.success(new ArrayList<>());
			}
			List<Integer> questionIds = JSON.parseArray(publish.getLibrary().getQuestionIds(), Integer.class);
			if (questionIds.isEmpty()) {
				return ResponseEntity.success(new ArrayList<>());
			}
			return ResponseEntity.success(mapper.findQuestionsByPreparationPublish(pid, mostWrong, questionIds));
		}
		return ResponseEntity.success(new ArrayList<>());
	}

	@Override
	public ResponseEntity findQuestionsByQuestionIds(RequestEntity entity) {
		Condition condition = new Condition(Question.class);
		condition.createCriteria()//
				.andIn("id", entity.getList("questionIds", Integer.class));
		List<Question> result = mapper.selectByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public void addQuestions(RequestEntity entity) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);
		List<Question> questions = entity.getList("questions", Question.class);
		try {
			int count = mapper.insertList(questions);

			Library library = libraryMapper.selectByPrimaryKey(entity.getInt("rid"));
			library.setNum(count);

			libraryMapper.updateByPrimaryKeySelective(library);
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

	@Override
	public ResponseEntity countQuestionByLibraryId(RequestEntity entity) {
		Condition condition = new Condition(Question.class);
		condition.createCriteria()//
				.andEqualTo("relateId", entity.getInt("rid"));
		int result = mapper.selectCountByCondition(condition);
		return ResponseEntity.success(result);
	}

}

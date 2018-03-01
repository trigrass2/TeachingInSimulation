package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.mapper.QuestionMapper;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.util.SpringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class QuestionServiceImpl extends AbstractService<Question> implements QuestionService {

	@Resource
	private LibraryService libraryService;

	@Override
	public PageInfo<Question> findQuestionsByLibrary(int pageIndex, int pageSize, int rid) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		PageHelper.startPage(pageIndex, pageSize);
		List<Question> result = findByCondition(condition);
		PageInfo<Question> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public List<Question> findQuestionsByLibrary(int rid) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		condition.orderBy("type").asc();
		return findByCondition(condition);
	}

	@Override
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		criteria.andEqualTo("type", type);
		return findByCondition(condition);
	}

	@Override
	public List<Question> findQuestionsByPublish(int pid, boolean mostWrong) {
		QuestionMapper questionMapper = (QuestionMapper) mapper;
		return questionMapper.findQuestionsByPublish(pid, mostWrong);
	}

	@Override
	public void addQuestions(int rid, List<Question> questions) {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = SpringUtil.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			int count = save(questions);

			Library library = libraryService.findById(rid);
			library.setNum(count);

			libraryService.update(library);
			transactionManager.commit(status);
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
		}
	}

	@Override
	public int countQuestionByLibrary(int rid) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		return getTotalBy(condition);
	}

}

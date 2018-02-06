package com.cas.sim.tis.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.QuestionService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class QuestionServiceImpl extends AbstractService<Question> implements QuestionService {

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
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		criteria.andEqualTo("type", type);
		return findByCondition(condition);
	}

	@Override
	public void addQuestions(List<Question> questions) {
		Date createDate = new Date();
		for (Question question : questions) {
			question.setCreateDate(createDate);
		}
		save(questions);
	}

	@Override
	public int countQuestionByLibrary(int rid) {
		Condition condition = new Condition(Question.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("relateId", rid);
		return getTotalBy(condition);
	}
	
	
}

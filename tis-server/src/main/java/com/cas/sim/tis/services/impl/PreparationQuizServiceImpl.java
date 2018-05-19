package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.PreparationQuizType;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.mapper.PreparationQuizMapper;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

import tk.mybatis.mapper.entity.Condition;

@Service
public class PreparationQuizServiceImpl implements PreparationQuizService {
	@Resource
	private PreparationQuizMapper mapper;

	@Override
	public ResponseEntity findQuizsByPreparationId(RequestEntity entity) {
		List<PreparationInfo> tests = mapper.findQuizsByPreparationId(entity.getInt("pid"));
		return ResponseEntity.success(tests);
	}

	@Override
	public ResponseEntity countFreeQuizByPreparationId(RequestEntity entity) {
		Condition condition = new Condition(PreparationQuiz.class);
		condition.createCriteria()//
				.andEqualTo("preparationId", entity.getInt("pid"))//
				.andEqualTo("type", PreparationQuizType.FREE.getType())//
				.andEqualTo("del", 0);
		int result = mapper.selectCountByCondition(condition);
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findPreparationQuizById(RequestEntity req) {
		int id = req.getInt("id");
		return ResponseEntity.success(mapper.selectByPrimaryKey(id));
	}

	@Override
	public void savePreparationQuiz(RequestEntity req) {
		PreparationQuiz quiz = req.getObject("quiz", PreparationQuiz.class);
		mapper.insertSelective(quiz);
	}

}

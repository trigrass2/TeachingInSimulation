package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cas.sim.tis.consts.PreparationQuizType;
import com.cas.sim.tis.entity.PreparationLibrary;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.mapper.PreparationLibraryMapper;
import com.cas.sim.tis.mapper.PreparationQuizMapper;
import com.cas.sim.tis.services.PreparationLibraryService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import tk.mybatis.mapper.entity.Condition;

@Service
public class PreparationLibraryServiceImpl implements PreparationLibraryService {

	@Resource
	private PreparationLibraryMapper mapper;
	@Resource
	private PreparationQuizMapper quizMapper;
	
	@Override
	public ResponseEntity findPreparationLibraryByPreparationId(RequestEntity entity) {
		List<PreparationLibrary> result = mapper.findPreparationLibraryByPreparationId(entity.getInt("pid"));
		return ResponseEntity.success(result);
	}

	@Override
	@Transactional
	public void addPreparationLibrary(RequestEntity entity) {
		PreparationLibrary library = entity.getObject("library", PreparationLibrary.class);
		mapper.insertUseGeneratedKeys(library);
		
		PreparationQuiz quiz = new PreparationQuiz();
		quiz.setPreparationId(entity.getInt("pid"));
		quiz.setRelationId(library.getId());
		quiz.setType(PreparationQuizType.QUESTION.getType());
		quiz.setCreator(library.getCreator());
		quizMapper.insert(quiz);
	}

	@Override
	public void updatePreparationLibrary(RequestEntity entity) {
		PreparationLibrary library = entity.getObject("library", PreparationLibrary.class);
		mapper.updateByPrimaryKeySelective(library);
	}

	@Override
	@Transactional
	public void deletePreparationLibrary(RequestEntity entity) {
		PreparationLibrary library = entity.getObject("library", PreparationLibrary.class);
		library.setDel(true);
		mapper.updateByPrimaryKeySelective(library);
		
		PreparationQuiz quiz = new PreparationQuiz();
		quiz.setDel(true);
		Condition condition = new Condition(PreparationQuiz.class);
		condition.createCriteria()//
		.andEqualTo("preparationId", entity.getInt("pid"))//
		.andEqualTo("relationId", library.getId())//
		.andEqualTo("type", PreparationQuizType.QUESTION.getType());
		quizMapper.updateByConditionSelective(quiz, condition);
	}

}

package com.cas.sim.tis.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.mapper.ExamLibraryAnswerMapper;
import com.cas.sim.tis.services.LibraryAnswerService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Service
public class ExamLibraryAnswerServiceImpl implements LibraryAnswerService {
	@Resource
	private ExamLibraryAnswerMapper mapper;

	@Override
	public ResponseEntity findAnswersByPublish(RequestEntity entity) {
		List<ExamLibraryAnswer> result = mapper.findAnswersByPublish(entity.getInt("pid"), entity.getInt("recordType"), entity.getBoolean("onlyWrong"));
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity statisticsByQuestionId(RequestEntity entity) {
		Map<AnswerState, Integer> statistics = new HashMap<>();
		for (AnswerState state : AnswerState.values()) {
			int num = mapper.statisticsByQuestionId(entity.getInt("pid"), entity.getInt("recordType"), entity.getInt("qid"), state.getType());
			statistics.put(state, num);
		}
		return ResponseEntity.success(statistics);
	}

}

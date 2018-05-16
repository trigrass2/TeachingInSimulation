package com.cas.sim.tis.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.mapper.LibraryAnswerMapper;
import com.cas.sim.tis.services.LibraryAnswerService;

@Service
public class LibraryAnswerServiceImpl extends AbstractService<LibraryAnswer> implements LibraryAnswerService {

	@Override
	public List<LibraryAnswer> findAnswersByPublish(int pid, boolean onlyWrong) {
		LibraryAnswerMapper answerMapper = (LibraryAnswerMapper) mapper;
		return answerMapper.findAnswersByPublish(pid, onlyWrong);
	}

	@Override
	public Map<AnswerState, Integer> statisticsByQuestionId(int pid, int qid) {
		LibraryAnswerMapper answerMapper = (LibraryAnswerMapper) mapper;

		Map<AnswerState, Integer> statistics = new HashMap<>();
		for (AnswerState state : AnswerState.values()) {
			int num = answerMapper.statisticsByQuestionId(pid, qid, state.getType());
			statistics.put(state, num);
		}
		return statistics;

	}

}

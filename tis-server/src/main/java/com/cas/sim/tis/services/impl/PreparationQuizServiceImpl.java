package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.mapper.PreparationQuizMapper;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.vo.PreparationInfo;
import com.cas.sim.tis.vo.PreparationQuizInfo;

@Service
public class PreparationQuizServiceImpl extends AbstractService<PreparationQuiz> implements PreparationQuizService {

	@Override
	public List<PreparationQuizInfo> findQuizsByPreparationId(Integer pid) {
		PreparationQuizMapper mapper = (PreparationQuizMapper) this.mapper;
		List<PreparationQuizInfo> quizInfos = mapper.findQuizsByPreparationId(pid);
		if (quizInfos == null) {
			return new ArrayList<>();
		} else {
			return quizInfos;
		}
	}

	@Override
	public List<PreparationInfo> findTestsByPreparationId(Integer pid) {
		PreparationQuizMapper mapper = (PreparationQuizMapper) this.mapper;
		List<PreparationInfo> tests = mapper.findTestsByPreparationId(pid);
		if (tests == null) {
			return new ArrayList<>();
		} else {
			return tests;
		}
	}

}

package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.vo.PreparationInfo;
import com.cas.sim.tis.vo.PreparationQuizInfo;

public interface PreparationQuizService extends BaseService<PreparationQuiz> {

	List<PreparationQuizInfo> findQuizsByPreparationId(Integer pid);

	List<PreparationInfo> findTestsByPreparationId(Integer pid);

}

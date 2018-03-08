package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.vo.PreparationInfo;

public interface PreparationQuizService extends BaseService<PreparationQuiz> {

	List<PreparationInfo> findQuizsByPreparationId(Integer pid);

	int countFreeQuizByPreparationId(Integer pid);

}

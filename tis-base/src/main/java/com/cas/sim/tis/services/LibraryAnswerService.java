package com.cas.sim.tis.services;

import java.util.List;
import java.util.Map;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;

public interface LibraryAnswerService extends BaseService<LibraryAnswer> {

	List<LibraryAnswer> findAnswersByPublish(int pid, boolean onlyWrong);

	Map<AnswerState, Integer> statisticsByQuestionId(int pid, int qid);

}

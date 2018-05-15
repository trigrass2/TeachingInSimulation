package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.services.LibraryAnswerService;

@Component
public class LibraryAnswerAction extends BaseAction {
	@Resource(name = "libraryAnswerService")
	private LibraryAnswerService service;

	public List<LibraryAnswer> findAnswersByPublish(int pid, boolean onlyWrong) {
		return service.findAnswersByPublish(pid, onlyWrong);
	}

	public Map<AnswerState, Integer> statisticsByQuestionId(int pid, int qid) {
		return service.statisticsByQuestionId(pid, qid);
	}
}

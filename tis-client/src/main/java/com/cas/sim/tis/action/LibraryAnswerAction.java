package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.services.LibraryAnswerService;

@Component
public class LibraryAnswerAction {
	@Resource
	@Qualifier("libraryAnswerServiceFactory")
	private RmiProxyFactoryBean libraryAnswerServiceFactory;

	public List<LibraryAnswer> findAnswersByPublish(int pid, boolean onlyWrong) {
		LibraryAnswerService service = (LibraryAnswerService) libraryAnswerServiceFactory.getObject();
		return service.findAnswersByPublish(pid, onlyWrong);
	}

	public Map<AnswerState, Integer> statisticsByQuestionId(int pid, int qid) {
		LibraryAnswerService service = (LibraryAnswerService) libraryAnswerServiceFactory.getObject();
		return service.statisticsByQuestionId(pid, qid);
	}

}

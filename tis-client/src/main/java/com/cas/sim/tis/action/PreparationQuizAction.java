package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.vo.PreparationInfo;
import com.cas.sim.tis.vo.PreparationQuizInfo;

@Component
public class PreparationQuizAction extends BaseAction<PreparationQuizService> {
	@Resource
	@Qualifier("preparationQuizServiceFactory")
	private RmiProxyFactoryBean preparationQuizServiceFactory;

	public List<PreparationQuizInfo> findQuizsByPreparationId(Integer pid) {
		PreparationQuizService service = getService();
		return service.findQuizsByPreparationId(pid);
	}

	public List<PreparationInfo> findTestsByPreparationId(Integer pid) {
		PreparationQuizService service = getService();
		return service.findTestsByPreparationId(pid);
	}

	public void addQuiz(PreparationQuiz quiz) {
		PreparationQuizService service = getService();
		quiz.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(quiz);
	}

	public void addQuizs(List<PreparationQuiz> quizs) {
		PreparationQuizService service = getService();
		service.save(quizs);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return preparationQuizServiceFactory;
	}
}

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

@Component
public class PreparationQuizAction extends BaseAction<PreparationQuizService> {
	@Resource
	@Qualifier("preparationQuizServiceFactory")
	private RmiProxyFactoryBean preparationQuizServiceFactory;

	public List<PreparationInfo> findQuizsByPreparationId(Integer pid) {
		PreparationQuizService service = getService();
		return service.findQuizsByPreparationId(pid);
	}

	public PreparationQuiz findQuizById(Integer id) {
		PreparationQuizService service = getService();
		return service.findById(id);
	}

	public void addQuiz(PreparationQuiz quiz) {
		PreparationQuizService service = getService();
		quiz.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(quiz);
	}

	public boolean checkFreeQuiz(Integer pid) {
		PreparationQuizService service = getService();
		int num = service.countFreeQuizByPreparationId(pid);
		return num > 0;
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return preparationQuizServiceFactory;
	}
}

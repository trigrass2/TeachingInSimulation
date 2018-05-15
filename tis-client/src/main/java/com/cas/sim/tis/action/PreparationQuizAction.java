package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationQuizAction extends BaseAction {
	@Resource(name = "preparationQuizService")
	private PreparationQuizService service;

	public List<PreparationInfo> findQuizsByPreparationId(Integer pid) {
		return service.findQuizsByPreparationId(pid);
	}

	public PreparationQuiz findQuizById(Integer id) {
		return service.findById(id);
	}

	public void addQuiz(PreparationQuiz quiz) {
		quiz.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(quiz);
	}

	public boolean checkFreeQuiz(Integer pid) {
		int num = service.countFreeQuizByPreparationId(pid);
		return num > 0;
	}

}

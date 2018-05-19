package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationQuiz;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationQuizAction extends BaseAction {
	@Resource
	private PreparationQuizService service;

	public List<PreparationInfo> findQuizsByPreparationId(Integer pid) {
		RequestEntity req = new RequestEntity()//
				.set("pid", pid)//
				.end();
		ResponseEntity resp = service.findQuizsByPreparationId(req);
		return JSON.parseArray(resp.data, PreparationInfo.class);
	}

	public PreparationQuiz findQuizById(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.end();
		ResponseEntity resp = service.findPreparationQuizById(req);
		return JSON.parseObject(resp.data, PreparationQuiz.class);
	}

	public void addQuiz(PreparationQuiz quiz) {
		quiz.setCreator(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntity()//
				.set("quiz", quiz)//
				.end();
		service.savePreparationQuiz(req);
	}

	public boolean checkFreeQuiz(Integer pid) {
		RequestEntity req = new RequestEntity()//
				.set("pid", pid)//
				.end();

		ResponseEntity resp = service.countFreeQuizByPreparationId(req);
		return JSON.parseObject(resp.data, Integer.class) > 0;
	}

}

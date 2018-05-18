package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.LibraryAnswer;
import com.cas.sim.tis.services.LibraryAnswerService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class LibraryAnswerAction extends BaseAction {
	@Resource
	private LibraryAnswerService service;

	public List<LibraryAnswer> findAnswersByPublish(int pid, boolean onlyWrong) {
		RequestEntity req = new RequestEntity();
		req.set("pid", pid).set("onlyWrong", onlyWrong).end();
		ResponseEntity resp = service.findAnswersByPublish(req);
		return JSON.parseArray(resp.data, LibraryAnswer.class);
	}

	public Map<AnswerState, Integer> statisticsByQuestionId(int pid, int qid) {
		RequestEntity req = new RequestEntity();
		req.set("pid", pid).set("qid", qid).end();
		ResponseEntity resp = service.statisticsByQuestionId(req);
		return JSON.parseObject(resp.data, new TypeReference<Map<AnswerState, Integer>>() {});
	}
}

package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.AnswerState;
import com.cas.sim.tis.entity.ExamLibraryAnswer;
import com.cas.sim.tis.services.LibraryAnswerService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class LibraryAnswerAction extends BaseAction {
	@Resource
	private LibraryAnswerService service;

	/**
	 * 根据条件查询试题答题结果
	 * @param pid 试题库发布编号
	 * @param onlyWrong 是否只查错题
	 * @return 答题结果集合
	 */
	public List<ExamLibraryAnswer> findAnswersByPublish(int pid, int recordType, boolean onlyWrong) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("recordType", recordType)//
				.set("onlyWrong", onlyWrong)//
				.build();
		ResponseEntity resp = service.findAnswersByPublish(req);
		return JSON.parseArray(resp.data, ExamLibraryAnswer.class);
	}

	/**
	 * 考核统计
	 * @param pid 试题库发布编号
	 * @param recordType 发布类型
	 * @param qid 试题编号
	 * @return 返回统计结果Map集合<br>
	 *         key:AnswerState<br>
	 *         value:人数
	 */
	public Map<AnswerState, Integer> statisticsByQuestionId(int pid, int recordType, int qid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("recordType", recordType)//
				.set("qid", qid)//
				.build();
		ResponseEntity resp = service.statisticsByQuestionId(req);
		return JSON.parseObject(resp.data, new TypeReference<Map<AnswerState, Integer>>() {});
	}
}

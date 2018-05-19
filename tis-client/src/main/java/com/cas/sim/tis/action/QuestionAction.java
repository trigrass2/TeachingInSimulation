package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@Component
public class QuestionAction extends BaseAction {
	@Resource
	private QuestionService service;

	/**
	 * 根据试题库分页查询试题
	 * @param pageIndex
	 * @param pageSize
	 * @param rid
	 * @return
	 */
	public List<Question> findQuestionsByLibrary(int pageIndex, int pageSize, int rid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.build();
//		分页信息
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		ResponseEntity resp = service.findQuestionsByLibrary(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	public List<Question> findQuestionsByLibrary(Integer rid) {
		return this.findQuestionsByLibrary(-1, 0, rid);
	}

	/**
	 * 根据试题库查询试题库下的所有试题
	 * @param rid
	 * @return
	 */
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.set("type", type)//
				.build();
		ResponseEntity resp = service.findQuestionsByLibraryAndQuestionType(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	public List<Question> findQuestionsByPublish(int pid, boolean mostWrong) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("mostWrong", mostWrong)//
				.build();
		ResponseEntity resp = service.findQuestionsByPublish(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 批量添加试题
	 * @param questions
	 */
	public void addQuestions(int rid, List<Question> questions) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("questions", questions)//
				.set("rid", rid)//
				.build();
		service.addQuestions(req);
	}

	/**
	 * 验证当前题库是否要添加试题
	 * @param rid
	 * @return
	 */
	public boolean checkImportOrExport(int rid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.build();
		ResponseEntity resp = service.countQuestionByLibrary(req);
		return Integer.parseInt(resp.data) > 0;
	}
}

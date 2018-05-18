package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageInfo;

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
	public PageInfo<Question> findQuestionsByLibrary(int pageIndex, int pageSize, int rid) {
		RequestEntity req = new RequestEntity();
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		req.set("rid", rid).end();
		ResponseEntity resp = service.findQuestionsByLibrary(req);
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Question>>() {});
	}

	public List<Question> findQuestionsByLibrary(Integer rid) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid).end();

		ResponseEntity resp = service.findQuestionsByLibrary(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 根据试题库查询试题库下的所有试题
	 * @param rid
	 * @return
	 */
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid).set("type", type).end();

		ResponseEntity resp = service.findQuestionsByLibraryAndQuestionType(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	public List<Question> findQuestionsByPublish(int pid, boolean mostWrong) {
		RequestEntity req = new RequestEntity();
		req.set("pid", pid).set("mostWrong", mostWrong).end();
		ResponseEntity resp = service.findQuestionsByPublish(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 批量添加试题
	 * @param questions
	 */
	public void addQuestions(int rid, List<Question> questions) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid).set("questions", questions).end();
		service.addQuestions(req);
	}

	/**
	 * 验证当前题库是否要添加试题
	 * @param rid
	 * @return
	 */
	public boolean checkImportOrExport(int rid) {
		RequestEntity req = new RequestEntity();
		req.set("rid", rid).end();
		ResponseEntity resp = service.countQuestionByLibrary(req);
		return JSON.parseObject(resp.data, Integer.class) > 0;
	}
}

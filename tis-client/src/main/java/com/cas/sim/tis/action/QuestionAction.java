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
	 * @param pageIndex 查询页
	 * @param pageSize 查询条数
	 * @param rid 试题库编号
	 * @return List 试题集合
	 */
	public List<Question> findQuestionsByLibraryId(int pageIndex, int pageSize, int rid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.build();
//		分页信息
		req.pageNum = pageIndex;
		req.pageSize = pageSize;
		ResponseEntity resp = service.findQuestionsByLibrary(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 不分页查询试题库试题集合
	 * @param rid 试题库编号
	 * @return List 试题集合
	 */
	public List<Question> findQuestionsByLibraryId(Integer rid) {
		return this.findQuestionsByLibraryId(-1, 0, rid);
	}

	/**
	 * 根据试题库编号查询试题库下的指定类型的试题集合
	 * @param rid 试题库编号
	 * @param type 试题类型
	 * @return List 试题集合
	 */
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.set("type", type)//
				.build();
		ResponseEntity resp = service.findQuestionsByLibraryAndQuestionType(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 根据发布编号查询试题集合
	 * @param pid 发布编号
	 * @param mostWrong 是否按错误最多排序
	 * @return 试题集合
	 */
	public List<Question> findQuestionsByPublishId(int pid, boolean mostWrong) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.set("mostWrong", mostWrong)//
				.build();
		ResponseEntity resp = service.findQuestionsByPublishId(req);
		return JSON.parseArray(resp.data, Question.class);
	}

	/**
	 * 批量添加试题
	 * @param rid 试题库编号
	 * @param questions 试题集合
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
	 * @param rid 试题库编号
	 * @return 是否存在试题，若存在则表示当前允许导出试题，若不存在则表示当前允许导入试题
	 */
	public boolean checkImportOrExport(int rid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("rid", rid)//
				.build();
		ResponseEntity resp = service.countQuestionByLibraryId(req);
		return Integer.parseInt(resp.data) > 0;
	}
}

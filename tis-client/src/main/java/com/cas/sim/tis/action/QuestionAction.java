package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.QuestionService;
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
		return service.findQuestionsByLibrary(pageIndex, pageSize, rid);
	}

	public List<Question> findQuestionsByLibrary(Integer rid) {
		return service.findQuestionsByLibrary(rid);
	}

	/**
	 * 根据试题库查询试题库下的所有试题
	 * @param rid
	 * @return
	 */
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		return service.findQuestionsByLibraryAndQuestionType(rid, type);
	}

	public List<Question> findQuestionsByPublish(int pid, boolean mostWrong) {
		return service.findQuestionsByPublish(pid, mostWrong);
	}

	/**
	 * 批量添加试题
	 * @param questions
	 */
	public void addQuestions(int rid, List<Question> questions) {
		service.addQuestions(rid, questions);
	}

	/**
	 * 验证当前题库是否要添加试题
	 * @param rid
	 * @return
	 */
	public boolean checkImportOrExport(int rid) {
		int total = service.countQuestionByLibrary(rid);
		return total > 0;
	}
}

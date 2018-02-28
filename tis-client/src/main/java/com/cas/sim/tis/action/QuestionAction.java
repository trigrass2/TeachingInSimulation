package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.entity.Question;
import com.cas.sim.tis.services.QuestionService;
import com.github.pagehelper.PageInfo;

@Component
public class QuestionAction {
	@Resource
	@Qualifier("questionServiceFactory")
	private RmiProxyFactoryBean questionServiceFactory;

	/**
	 * 根据试题库分页查询试题
	 * @param pageIndex
	 * @param pageSize
	 * @param rid
	 * @return
	 */
	public PageInfo<Question> findQuestionsByLibrary(int pageIndex, int pageSize, int rid) {
		QuestionService service = (QuestionService) questionServiceFactory.getObject();
		return service.findQuestionsByLibrary(pageIndex, pageSize, rid);
	}

	/**
	 * 根据试题库查询试题库下的所有试题
	 * @param rid
	 * @return
	 */
	public List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type) {
		QuestionService service = (QuestionService) questionServiceFactory.getObject();
		return service.findQuestionsByLibraryAndQuestionType(rid, type);
	}

	public List<Question> findQuestionsByPublish(int pid) {
		QuestionService service = (QuestionService) questionServiceFactory.getObject();
		return service.findQuestionsByPublish(pid);
	}

	/**
	 * 批量添加试题
	 * @param questions
	 */
	public void addQuestions(int rid, List<Question> questions) {
		QuestionService service = (QuestionService) questionServiceFactory.getObject();
		service.addQuestions(rid, questions);
	}

	/**
	 * 验证当前题库是否要添加试题
	 * @param rid
	 * @return
	 */
	public boolean checkImportOrExport(int rid) {
		QuestionService service = (QuestionService) questionServiceFactory.getObject();
		int total = service.countQuestionByLibrary(rid);
		return total > 0;
	}
}
package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Question;
import com.github.pagehelper.PageInfo;

public interface QuestionService extends BaseService<Question> {

	PageInfo<Question> findQuestionsByLibrary(int pageIndex, int pageSize, int rid);

	List<Question> findQuestionsByLibrary(int rid);

	List<Question> findQuestionsByLibraryAndQuestionType(int rid, int type);

	void addQuestions(int rid,List<Question> questions);

	int countQuestionByLibrary(int rid);
}

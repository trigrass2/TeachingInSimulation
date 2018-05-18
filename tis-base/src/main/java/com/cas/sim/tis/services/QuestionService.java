package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface QuestionService {

	/**
	 * @param rid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByLibrary(RequestEntity entity);

	/**
	 * @param rid
	 * @param type
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByLibraryAndQuestionType(RequestEntity entity);

	/**
	 * @param pid
	 * @param mostWrong
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByPublish(RequestEntity entity);

	/**
	 * @param rid
	 * @param questions
	 */
	@ThriftMethod
	void addQuestions(RequestEntity entity);

	/**
	 * @param rid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countQuestionByLibrary(RequestEntity entity);
}

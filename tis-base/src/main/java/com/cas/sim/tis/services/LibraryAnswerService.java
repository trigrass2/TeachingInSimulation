package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryAnswerService {
	/**
	 * @param pid
	 * @param onlyWrong
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findAnswersByPublish(RequestEntity entity);

	/**
	 * @param pid
	 * @param qid
	 * @return
	 */
	@ThriftMethod
	ResponseEntity statisticsByQuestionId(RequestEntity entity);

}

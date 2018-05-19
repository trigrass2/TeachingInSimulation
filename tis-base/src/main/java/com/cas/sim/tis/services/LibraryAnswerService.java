package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryAnswerService {
	/**
	 * 根据条件查询试题答题结果
	 * @param pid 试题库发布编号
	 * @param onlyWrong 是否只查错题
	 * @return 答题结果集合
	 */
	@ThriftMethod
	ResponseEntity findAnswersByPublish(RequestEntity entity);

	/**
	 * 考核统计
	 * @param pid 试题库发布编号
	 * @param qid 试题编号
	 * @return 返回统计结果Map集合<br>
	 *         key:AnswerState<br>
	 *         value:人数
	 */
	@ThriftMethod
	ResponseEntity statisticsByQuestionId(RequestEntity entity);

}

package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface QuestionService {

	/**
	 * 根据试题库分页查询试题
	 * @param entity （pageIndex 查询页；pageSize 查询条数；rid 试题库编号）
	 * @return List 试题集合
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByLibrary(RequestEntity entity);

	/**
	 * 根据试题库编号查询试题库下的指定类型的试题集合
	 * @param entity （rid 试题库编号；type 试题类型）
	 * @return List 试题集合
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByLibraryAndQuestionType(RequestEntity entity);

	/**
	 * 根据发布编号查询试题集合
	 * @param entity （pid 发布编号；mostWrong 是否按错误最多排序）
	 * @return List 试题集合
	 */
	@ThriftMethod
	ResponseEntity findQuestionsByPublishId(RequestEntity entity);

	/**
	 * 批量新增试题
	 * @param entity （rid 试题库编号；questions 试题集合）
	 */
	@ThriftMethod
	void addQuestions(RequestEntity entity);

	/**
	 * 统计当前试题库下的试题数量
	 * @param rid 试题库编号
	 * @return int 试题数量
	 */
	@ThriftMethod
	ResponseEntity countQuestionByLibraryId(RequestEntity entity);
}

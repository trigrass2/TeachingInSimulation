package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryService {

	/**
	 * 按条件查询题库信息集合
	 * @param type 题库类型
	 * @param keyword 关键词查询
	 * @param pageNum 当前查询页数
	 * @param pageSize 当前查询条数
	 * @return 若pageNum=-1返回List集合，反之返回PageInfo(id, name, time, num, createDate)
	 */
	@ThriftMethod
	ResponseEntity findLibraryByType(RequestEntity entity);

	/**
	 * 根据试题库编号查询试题库信息对象
	 * @param id 试题库编号
	 * @return 试题库信息对象
	 */
	@ThriftMethod
	ResponseEntity findLibraryById(RequestEntity req);

	/**
	 * 新增题库信息
	 * @param library 试题库信息对象
	 */
	@ThriftMethod
	void savelibrary(RequestEntity req);

	/**
	 * 修改题库信息
	 * @param library 试题库信息对象
	 */
	@ThriftMethod
	void updatelibrary(RequestEntity req);

}

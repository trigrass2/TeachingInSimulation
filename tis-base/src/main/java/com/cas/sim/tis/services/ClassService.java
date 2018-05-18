package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ClassService {

	/**
	 * 根据ID查询班级信息
	 * @param id
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findClassById(RequestEntity entity);

	/**
	 * 分页查询班级信息
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findClasses(RequestEntity entity);

	/**
	 * 根据教师编号查询班级集合
	 * @param teacherId
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findClassesByTeacherId(RequestEntity entity);

	/**
	 * 批量保存班级信息
	 * @param infos
	 * @param creator
	 */
	@ThriftMethod
	void saveClasses(RequestEntity entity);

	/**
	 * 修改班级信息
	 * @param clazz
	 */
	@ThriftMethod
	void modifyClass(RequestEntity entity);

}

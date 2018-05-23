package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ClassService {

	/**
	 * 根据ID查询班级信息
	 * @param entity （id 班级编号）
	 * @return 班级信息对象
	 */
	@ThriftMethod
	ResponseEntity findClassById(RequestEntity entity);

	/**
	 * 分页查询班级信息
	 * @param entity （pageIndex 当前页；pageSize 查询条数）
	 * @return 分页对象
	 */
	@ThriftMethod
	ResponseEntity findClasses(RequestEntity entity);

	/**
	 * 根据教师编号查询班级集合
	 * @param entity （teacherId 教师编号）
	 * @return 班级信息集合
	 */
	@ThriftMethod
	ResponseEntity findClassesByTeacherId(RequestEntity entity);

	@ThriftMethod
	void addClass(RequestEntity entity);

	/**
	 * 批量保存班级信息
	 * @param entity （infos 班级导入信息对象；creator 创建人）
	 */
	@ThriftMethod
	void saveClasses(RequestEntity entity);

	/**
	 * 修改班级信息
	 * @param entity （clazz 班级信息对象）
	 */
	@ThriftMethod
	void modifyClass(RequestEntity entity);

}

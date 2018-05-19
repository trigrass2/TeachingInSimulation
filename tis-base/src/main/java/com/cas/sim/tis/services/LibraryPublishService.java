package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryPublishService {
	/**
	 * 根据试题考核发布编号查询试题考核发布对象
	 * @param entity（id 试题考核发布编号）
	 * @return 试题考核发布对象
	 */
	@ThriftMethod
	ResponseEntity findPublishById(RequestEntity entity);

	/**
	 * 根据教师编号获得该用户发布的试题考核
	 * @param entity（pageIndex 当前页； pageSize 查询条数； creator 教师编号）
	 * @return 教师查看的分页集合
	 */
	@ThriftMethod
	ResponseEntity findPublishForTeacher(RequestEntity entity);

	/**
	 * 根据学生编号获得该用户发布的试题练习/参与的试题考核
	 * @param entity（ pageIndex 当前页； pageSize 查询条数； type 试题发布类型：PublishType； creator 学生编号）
	 * @return 学生查看的分页集合
	 */
	@ThriftMethod
	ResponseEntity findPublishForStudent(RequestEntity entity);

	/**
	 * 根据试题考核发布编号获得学生提交状态
	 * @param entity（ id 试题考核发布编号）
	 * @return 学生提交状态集合
	 */
	@ThriftMethod
	ResponseEntity findSubmitStateById(RequestEntity entity);

	/**
	 * 教师发布考核，并向考核班级所有学生广播考核开始
	 * @param entity（ publish 试题考核发布对象）
	 * @return 试题考核发布编号
	 */
	@ThriftMethod
	ResponseEntity publishLibraryToClass(RequestEntity entity);

	/**
	 * 学生发布练习
	 * @param entity（ publish 试题考核发布对象）
	 * @return 试题考核发布编号
	 */
	@ThriftMethod
	ResponseEntity practiceLibraryByStudent(RequestEntity entity);

	/**
	 * 服务器端统一结束试题考核，根据试题发布对象编号修改试题发布状态
	 * @param entity（id 试题发布对象编号）
	 * @return 试题发布对象
	 */
	@ThriftMethod
	ResponseEntity updatePublishLibrary(RequestEntity entity);
}

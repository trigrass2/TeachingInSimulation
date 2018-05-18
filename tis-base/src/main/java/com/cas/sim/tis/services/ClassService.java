package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ClassService {

	/**
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findClasses(RequestEntity entity);

	/**
	 * @param teacherId
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findClassesByTeacher(RequestEntity entity);

	/**
	 * @param infos
	 * @param creator
	 */
	@ThriftMethod
	void saveClasses(RequestEntity entity);

	/**
	 * @param clazz
	 */
	@ThriftMethod
	void modifyClass(RequestEntity entity);
}

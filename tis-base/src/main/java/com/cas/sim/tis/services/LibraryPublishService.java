package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface LibraryPublishService {
	/**
	 * @param id
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findPublishById(RequestEntity entity);

	/**
	 * @param pageIndex
	 * @param pageSize
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findPublishForTeacher(RequestEntity entity);

	/**
	 * @param pageIndex
	 * @param pageSize
	 * @param type
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findPublishForStudent(RequestEntity entity);

	/**
	 * @param id
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findSubmitStateById(RequestEntity entity);

	/**
	 * @param publish
	 * @return
	 */
	@ThriftMethod
	ResponseEntity publishLibraryToClass(RequestEntity entity);

	/**
	 * @param publish
	 * @return
	 */
	@ThriftMethod
	ResponseEntity practiceLibraryByStudent(RequestEntity entity);

	@ThriftMethod
	ResponseEntity publishLibrary(RequestEntity entity);
}

package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ResourceService {
	@ThriftMethod
//	ThriftEntity findResourceInfoById(int id);
	ResponseEntity findResourceInfoById(RequestEntity entity);

	/**
	 * @param pagination
	 * @param pageSize
	 * @param resourceTypes
	 * @param keyword
	 * @param orderByClause
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findResourcesByCreator(RequestEntity entity);

	/**
	 * @param pagination
	 * @param pageSize
	 * @param resourceTypes
	 * @param keyword
	 * @param orderByClause
	 * @param creater
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findResourcesByBrowseHistory(RequestEntity entity);

	/**
	 * @param pagination
	 * @param pageSize
	 * @param resourceTypes
	 * @param keyword
	 * @param orderByClause
	 * @param creater
	 * @return
	 */
	@ThriftMethod
	ResponseEntity findResourcesByCollection(RequestEntity entity);

	/**
	 * @param type
	 * @param keyword
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countResourceByType(RequestEntity entity);

	/**
	 * @param type
	 * @param keyword
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countBrowseResourceByType(RequestEntity entity);

	/**
	 * @param type
	 * @param keyword
	 * @param creator
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countCollectionResourceByType(RequestEntity entity);

	/**
	 * @param resources
	 * @return
	 */
	@ThriftMethod
	List<Integer> addResources(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	void browsed(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	void uncollect(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	void collected(RequestEntity entity);

	/**
	 * @param id
	 */
	@ThriftMethod
	void deteleResource(RequestEntity entity);
}

package com.cas.sim.tis.services;

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
	ResponseEntity addResources(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	ResponseEntity browsed(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	ResponseEntity uncollect(RequestEntity entity);

	/**
	 * @param id
	 * @param userId
	 */
	@ThriftMethod
	ResponseEntity collected(RequestEntity entity);

	/**
	 * @param id
	 */
	@ThriftMethod
	ResponseEntity deteleResource(RequestEntity entity);
}

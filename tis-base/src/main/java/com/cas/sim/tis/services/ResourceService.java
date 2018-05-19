package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ResourceService {
	@ThriftMethod
	ResponseEntity findResourceById(RequestEntity entity);

	@ThriftMethod
	ResponseEntity findResourceInfoById(RequestEntity entity);

	@ThriftMethod
	ResponseEntity findResourceByIds(RequestEntity entity);

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
	 * 各类资源浏览数量
	 * @param req
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countBrowseResourceByTypes(RequestEntity req);

	/**
	 * 各类资源收藏数量
	 * @param req
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countCollectionResourceByTypes(RequestEntity req);

	/**
	 * 各类资源总数量
	 * @param req
	 * @return
	 */
	@ThriftMethod
	ResponseEntity countResourceByTypes(RequestEntity req);

	/**
	 * @param resources
	 * @return 返回上传资源的ID集合
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

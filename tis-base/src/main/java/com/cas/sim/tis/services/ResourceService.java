package com.cas.sim.tis.services;

import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import io.airlift.drift.annotations.ThriftMethod;
import io.airlift.drift.annotations.ThriftService;

@ThriftService
public interface ResourceService {
	/**
	 * 根据资源编号查询资源对象
	 * @param entity（id 资源编号）
	 * @return Resource 资源对象
	 */
	@ThriftMethod
	ResponseEntity findResourceById(RequestEntity entity);

	/**
	 * 根据资源编号查询资源相关信息对象
	 * @param entity （id 资源编号）
	 * @return ResourceInfo 资源相关信息对象
	 */
	@ThriftMethod
	ResponseEntity findResourceInfoById(RequestEntity entity);

	/**
	 * 根据资源编号集合查询资源集合
	 * @param entity （ids 资源编号集合）
	 * @return List 资源集合
	 */
	@ThriftMethod
	ResponseEntity findResourceByIds(RequestEntity entity);

	/**
	 * 按条件分页查询资源集合
	 * @param entity （type 资源所属类型；pageNum 查询页；pageSize 查询条数；resourceTypes 资源类型集合；keyword 查询关键字；orderByClause 排序条件；creator 创建人）
	 * @return PageInfo Resource资源集合
	 */
	@ThriftMethod
	ResponseEntity findResourcesByCreator(RequestEntity entity);

	/**
	 * 按条件分页查询浏览历史记录资源集合
	 * @param entity （type 资源所属类型；pageNum 查询页；pageSize 查询条数；resourceTypes 资源类型集合；keyword 查询关键字；orderByClause 排序条件；creator 创建人）
	 * @return PageInfo Resource资源集合
	 */
	@ThriftMethod
	ResponseEntity findResourcesByBrowseHistory(RequestEntity entity);

	/**
	 * 按条件分页查询收藏记录资源集合
	 * @param entity （type 资源所属类型；pageNum 查询页；pageSize 查询条数；resourceTypes 资源类型集合；keyword 查询关键字；orderByClause 排序条件；creator 创建人）
	 * @return PageInfo Resource资源集合
	 */
	@ThriftMethod
	ResponseEntity findResourcesByCollection(RequestEntity entity);

	/**
	 * 按资源类型统计资源数量
	 * @param entity （menuType 菜单类型；type 资源类型；keyword 查询关键字；creator 创建人）
	 * @return int 资源数量
	 */
	@ThriftMethod
	ResponseEntity countResourceByType(RequestEntity entity);

	/**
	 * 按资源类型统计浏览资源数量
	 * @param entity （menuType 菜单类型；type 资源类型；keyword 查询关键字；creator 创建人）
	 * @return int 资源数量
	 */
	@ThriftMethod
	ResponseEntity countBrowseResourceByType(RequestEntity entity);

	/**
	 * 按资源类型统计收藏资源数量
	 * @param entity （menuType 菜单类型；type 资源类型；keyword 查询关键字；creator 创建人）
	 * @return int 资源数量
	 */
	@ThriftMethod
	ResponseEntity countCollectionResourceByType(RequestEntity entity);

	/**
	 * 按条件分页查询资源浏览记录集合
	 * @param req （menuType 菜单类型；types 资源类型集合；keyword 查询关键字；creator 创建人）
	 * @return Map资源数量集合<br>
	 *         Key:资源类型<br>
	 *         Value:对应数量
	 */
	@ThriftMethod
	ResponseEntity countBrowseResourceByTypes(RequestEntity req);

	/**
	 * 按条件分页查询资源收藏记录集合
	 * @param req （menuType 菜单类型；types 资源类型集合；keyword 查询关键字；creator 创建人）
	 * @return Map资源数量集合<br>
	 *         Key:资源类型<br>
	 *         Value:对应数量
	 */
	@ThriftMethod
	ResponseEntity countCollectionResourceByTypes(RequestEntity req);

	/**
	 * 按条件分页查询资源集合
	 * @param req （menuType 菜单类型；types 资源类型集合；keyword 查询关键字；creator 创建人）
	 * @return Map资源数量集合<br>
	 *         Key:资源类型<br>
	 *         Value:对应数量
	 */
	@ThriftMethod
	ResponseEntity countResourceByTypes(RequestEntity req);
	
	/**
	 * 新增资源
	 * @param entity （resource 资源对象）
	 * @return 返回新增资源的ID集合
	 */
	@ThriftMethod
	ResponseEntity addResource(RequestEntity entity);

	/**
	 * 批量新增资源
	 * @param entity （resources 资源集合）
	 * @return 返回新增资源的ID集合
	 */
	@ThriftMethod
	ResponseEntity addResources(RequestEntity entity);

	/**
	 * 添加浏览记录
	 * @param entity （id 资源编号；creator 创建人）
	 */
	@ThriftMethod
	ResponseEntity browsed(RequestEntity entity);

	/**
	 * 取消收藏记录
	 * @param entity （id 资源编号；creator 创建人）
	 */
	@ThriftMethod
	ResponseEntity uncollect(RequestEntity entity);

	/**
	 * 添加收藏记录
	 * @param entity （id 资源编号；creator 创建人）
	 */
	@ThriftMethod
	ResponseEntity collected(RequestEntity entity);

	/**
	 * 修改资源对象
	 * @param entity （resource 资源对象）
	 */
	@ThriftMethod
	ResponseEntity updateResource(RequestEntity entity);
}

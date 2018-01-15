package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.User;
import com.github.pagehelper.PageInfo;

public interface ResourceService extends BaseService<Resource> {
	/**
	 * @param user 当前用户
	 * @param pagination 当前页码
	 * @param pageSize 每一页显示的条数
	 * @param resourceTypes 资源类型
	 * @return
	 * @throws RemoteException
	 */
	PageInfo<Resource> findResources(User user, int pagination, int pageSize, List<Integer> resourceTypes);

	/**
	 * @param user 当前用户
	 * @param pagination 当前页码
	 * @param pageSize 每一页显示的条数
	 * @param resourceTypes 资源类型
	 * @param keyWord 关键字查询
	 * @return
	 * @throws RemoteException
	 */
	PageInfo<Resource> findResources(User user, int pagination, int pageSize, List<Integer> resourceTypes, String keyWord);
}

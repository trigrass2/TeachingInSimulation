package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Collection;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.vo.ResourceInfo;
import com.github.pagehelper.PageInfo;

public interface ResourceService extends BaseService<Resource> {
	ResourceInfo findResourceInfoByID(int id);

	PageInfo<Resource> findResourcesByCreator(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, List<Integer> creators);

	int countResourceByType(int type, String keyword, List<Integer> creators);

	boolean addResource(Resource resource);

	void browsed(Integer id);

	void uncollect(Integer id, Integer userId);
	
	void collected(Collection collection);
	
	void deteleResource(Integer id);
}

package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.entity.User;
import com.github.pagehelper.PageInfo;

public interface ResourceService extends BaseService<Resource> {
	PageInfo<Resource> findAdminResources(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause);

	PageInfo<Resource> findTeacherResources(int UserId, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause);

	PageInfo<Resource> findStudentResources(int UserId, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause);

	int countAdminResourceByType(int type, List<Integer> resourceTypes, String keyword);
	int countTeacherResourceByType(int UserId,int type, List<Integer> resourceTypes, String keyword);
	int countStudentResourceByType(int userId,int type, List<Integer> resourceTypes, String keyword);
}

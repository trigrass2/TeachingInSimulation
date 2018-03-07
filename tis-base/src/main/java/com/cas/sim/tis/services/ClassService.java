package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.vo.ClassInfo;
import com.github.pagehelper.PageInfo;

public interface ClassService extends BaseService<com.cas.sim.tis.entity.Class> {

	PageInfo<Class> findClasses(int pageIndex, int pageSize);

	List<Class> findClassesByTeacher(int teacherId);

	void saveClasses(List<ClassInfo> infos, Integer creator);

	void modifyClass(Class clazz);
}

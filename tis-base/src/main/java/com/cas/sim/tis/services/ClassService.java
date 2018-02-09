package com.cas.sim.tis.services;

import java.util.List;

import com.cas.sim.tis.entity.Class;
import com.github.pagehelper.PageInfo;

public interface ClassService extends BaseService<com.cas.sim.tis.entity.Class> {

	PageInfo<Class> findClasses(int pageIndex, int pageSize);

	void addClasses(List<Class> classes);

	void modifyClass(Class clazz);}

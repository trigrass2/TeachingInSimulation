package com.cas.sim.tis.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Class;
import com.cas.sim.tis.services.ClassService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("classService")
public class ClassServiceImpl extends AbstractService<Class> implements ClassService {

	@Override
	public PageInfo<Class> findClasses(int pageIndex, int pageSize) {
		Condition condition = new Condition(Class.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("del", 0);
		PageHelper.startPage(pageIndex, pageSize, "CREATE_DATE DESC");
		List<Class> result = findByCondition(condition);
		PageInfo<Class> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", page.getTotal(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public void addClasses(List<Class> classes) {
		Date createDate = new Date();
		for (Class clazz : classes) {
			clazz.setCreateDate(createDate);
		}
		save(classes);
	}

	@Override
	public void modifyClass(Class clazz) {
		clazz.setUpdateDate(new Date());
		update(clazz);
	}

}

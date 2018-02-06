package com.cas.sim.tis.services.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.services.LibraryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class LibraryServiceImpl extends AbstractService<Library> implements LibraryService {

	@Override
	public PageInfo<Library> findLibraryByType(int pageIndex, int pageSize, int type) {
		Condition condition = new Condition(Library.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("type", type);
		criteria.andEqualTo("del", 0);

		PageHelper.startPage(pageIndex, pageSize);
		List<Library> result = findByCondition(condition);
		PageInfo<Library> page = new PageInfo<Library>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public void addLibrary(Library library) {
		library.setCreateDate(new Date());
		save(library);
	}

	@Override
	public void modifyLibrary(Library library) {
		library.setUpdateDate(new Date());
		update(library);
	}

}

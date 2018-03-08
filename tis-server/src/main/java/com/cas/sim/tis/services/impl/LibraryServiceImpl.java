package com.cas.sim.tis.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
	public List<Library> findLibraryByType(int type, String key) {
		Condition condition = new Condition(Library.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("type", type);
		criteria.andEqualTo("del", 0);
		if (!StringUtils.isEmpty(key)) {
			criteria.andLike("name", "%" + key + "%");
		}
		List<Library> result = findByCondition(condition);
		if (result == null) {
			return new ArrayList<>();
		} else {
			return result;
		}
	}

}

package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.LibraryPublish;
import com.cas.sim.tis.mapper.LibraryPublishMapper;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.vo.LibraryPublishForStudent;
import com.cas.sim.tis.vo.LibraryPublishForTeacher;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class LibraryPublishServiceImpl extends AbstractService<LibraryPublish> implements LibraryPublishService {

	@Override
	public PageInfo<LibraryPublishForTeacher> findPublishForTeacher(int pageIndex, int pageSize, int creator) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(pageIndex, pageSize);
		List<LibraryPublishForTeacher> result = publishMapper.findPublishForTeacher(creator);
		PageInfo<LibraryPublishForTeacher> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}

	@Override
	public PageInfo<LibraryPublishForStudent> findPublishForStudent(int pageIndex, int pageSize, int type, int creator) {
		LibraryPublishMapper publishMapper = (LibraryPublishMapper) mapper;
		PageHelper.startPage(pageIndex, pageSize);
		List<LibraryPublishForStudent> result = publishMapper.findPublishForStudent(type, creator);
		PageInfo<LibraryPublishForStudent> page = new PageInfo<>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), pageIndex, pageSize, page.getPages());
		return page;
	}
	

}

package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class LibraryServiceImpl implements LibraryService {

	private static final Logger LOG = LoggerFactory.getLogger(LibraryServiceImpl.class);

	@Resource
	private LibraryMapper mapper;

	@Override
	public ResponseEntity findLibraryByType(RequestEntity entity) {
		Condition condition = new Condition(Library.class);
		Criteria criteria = condition.createCriteria()//
				.andEqualTo("type", entity.getInt("type"))//
				.andEqualTo("del", 0);
		String keyword = entity.getString("keyword");
		if (!StringUtils.isEmpty(keyword)) {
			criteria.andLike("name", String.format("%%%s%%", keyword));
		}
		PageHelper.startPage(entity.pageNum, entity.pageSize);
		List<Library> result = mapper.selectByCondition(condition);
		PageInfo<Library> page = new PageInfo<Library>(result);
		LOG.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
		return ResponseEntity.success(result);
	}
}

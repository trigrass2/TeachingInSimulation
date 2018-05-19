package com.cas.sim.tis.services.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.entity.Library;
import com.cas.sim.tis.mapper.LibraryMapper;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class LibraryServiceImpl implements LibraryService {

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
		if (entity.pageNum != -1) {
			PageHelper.startPage(entity.pageNum, entity.pageSize);
		}
		List<Library> result = mapper.selectByCondition(condition);
		if (entity.pageNum != -1) {
			PageInfo<Library> page = new PageInfo<Library>(result);
			log.info("成功查找到{}条资源,当前页码{},每页{}条资源,共{}页", result.size(), entity.pageNum, entity.pageSize, page.getPages());
			return ResponseEntity.success(page);
		}
		return ResponseEntity.success(result);
	}

	@Override
	public ResponseEntity findLibraryById(RequestEntity req) {
		int id = req.getInt("id");
		return ResponseEntity.success(mapper.selectByPrimaryKey(id));
	}

	@Override
	public void savelibrary(RequestEntity req) {
		Library library = req.getObject("library", Library.class);
		mapper.insert(library);
	}

	@Override
	public void updatelibrary(RequestEntity req) {
		Library library = req.getObject("library", Library.class);
		mapper.updateByPrimaryKeySelective(library);
	}
}

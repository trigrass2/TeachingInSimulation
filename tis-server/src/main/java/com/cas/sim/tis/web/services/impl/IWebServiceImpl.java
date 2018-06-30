package com.cas.sim.tis.web.services.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cas.sim.tis.mapper.IMapper;
import com.cas.sim.tis.web.services.IWebService;

public abstract class IWebServiceImpl<T> implements IWebService<T> {

	@Autowired
	protected IMapper<T> mapper;

	public IMapper<T> getMapper() {
		return mapper;
	}

	@Override
	public T selectByKey(Object key) {
		return mapper.selectByPrimaryKey(key);
	}

	@Override
	public T selectOne(Object example) {
		List<T> list=selectByCondition(example);
		if(!list.isEmpty()){
			return list.get(0);
		}else{
			return null;
		}
	}

	@Override
	public List<T> selectAll() {
		return mapper.selectAll();
	}

	@Override
	public int save(T entity) {
		return mapper.insert(entity);
	}

	@Override
	public int saveByPrimaryKey(T entity) {
		return mapper.insertUseGeneratedKeys(entity);
	}
	
	@Override
	public int saveAll(List<T> dataList) {
		return mapper.insertList(dataList);
	}

	@Override
	public int delete(Object key) {
		return mapper.deleteByPrimaryKey(key);
	}

	@Override
	public int updateAll(T entity) {
		return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public int updateNotNull(T entity) {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public List<T> selectByCondition(Object condition) {
		return mapper.selectByCondition(condition);
	}

	@Override
	public int selectCountByCondition(Object condition) {
		return mapper.selectCountByCondition(condition);
	}

	@Override
	public List<T> selectListByEntity(T entity) {
		return mapper.select(entity);
	}
}
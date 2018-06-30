package com.cas.sim.tis.web.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWebService<T> {
	T selectByKey(Object key);

	T selectOne(Object example);

	List<T> selectAll();

	int save(T entity);
	
	int saveByPrimaryKey(T entity);

	int saveAll(List<T> dataList);

	int delete(Object key);

	int updateAll(T entity);

	int updateNotNull(T entity);

	List<T> selectByCondition(Object condition);

	int selectCountByCondition(Object condition);

	List<T> selectListByEntity(T entity);

}

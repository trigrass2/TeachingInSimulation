package com.cas.sim.tis.services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.TooManyResultsException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cas.sim.tis.mapper.IMapper;
import com.cas.sim.tis.services.BaseService;

import tk.mybatis.mapper.entity.Condition;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 */
public abstract class AbstractService<T> implements BaseService<T> {

	@Autowired
	protected IMapper<T> mapper;

	private Class<T> modelClass; // 当前泛型真实类型的Class

	public AbstractService() {
		ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
		modelClass = (Class<T>) pt.getActualTypeArguments()[0];
	}

	@Override
	public int getTotal() {
		return mapper.selectCount(null);
	}

	@Override
	public int getTotalBy(Condition condition) {
		return mapper.selectCountByCondition(condition);
	}

	public void save(T model) {
		mapper.insertSelective(model);
	}

	public void save(List<T> models) {
		mapper.insertList(models);
	}

	public void deleteById(Integer id) {
		mapper.deleteByPrimaryKey(id);
	}

	public void deleteByIds(String ids) {
		mapper.deleteByIds(ids);
	}

	public void update(T model) {
		mapper.updateByPrimaryKeySelective(model);
	}

	public T findById(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public T findBy(String fieldName, Object value) throws TooManyResultsException {
		try {
			T model = modelClass.newInstance();
			Field field = modelClass.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(model, value);
			return mapper.selectOne(model);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}

	public List<T> findByIds(String ids) {
		List<T> dataList = mapper.selectByIds(ids);
		if (dataList == null) {
			dataList = new ArrayList<T>();
		}
		return dataList;
	}

	public List<T> findByCondition(Condition condition) {
		List<T> dataList = mapper.selectByCondition(condition);
		if (dataList == null) {
			dataList = new ArrayList<T>();
		}
		return dataList;
	}

	public List<T> findAll() {
		List<T> dataList = mapper.selectAll();
		if (dataList == null) {
			dataList = new ArrayList<T>();
		}
		return dataList;
	}
}

package com.cas.sim.tis.mapper;

import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.example.SelectOneByExampleMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

public interface IMapper<T>extends BaseMapper<T>,SelectOneByExampleMapper<T>, ConditionMapper<T>, IdsMapper<T>, InsertListMapper<T>, InsertUseGeneratedKeysMapper<T> {
	void insertRetPK(T t);
}

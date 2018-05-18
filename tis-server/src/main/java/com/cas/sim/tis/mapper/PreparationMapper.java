package com.cas.sim.tis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.cas.sim.tis.entity.Preparation;

import tk.mybatis.mapper.common.example.SelectOneByExampleMapper;

@Mapper
public interface PreparationMapper extends IMapper<Preparation>, SelectOneByExampleMapper<Preparation> {

}

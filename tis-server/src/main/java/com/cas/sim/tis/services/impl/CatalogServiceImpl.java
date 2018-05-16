package com.cas.sim.tis.services.impl;

import java.util.Collections;
import java.util.List;

import com.alibaba.dubbo.config.annotation.Service;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.mapper.CatalogMapper;
import com.cas.sim.tis.services.CatalogService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CatalogServiceImpl extends AbstractService<Catalog> implements CatalogService {

	@Override
	public List<Catalog> findCatalogsByParentId(Integer rid) {
		Condition condition = new Condition(Catalog.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("rid", rid);
		criteria.andEqualTo("del", 0);
		
		CatalogMapper catalogMapper = (CatalogMapper) mapper;
		catalogMapper.selectByCondition(condition);
		
		List<Catalog> catalogs = null;
		try {
			catalogs = catalogMapper.selectByCondition(condition);
			LOG.debug("查询到子节点数量：{}", catalogs.size());
		} catch (Exception e) {
			LOG.error("查询ID{}下子节点失败！", rid);
			catalogs = Collections.emptyList();
		}
		return catalogs;
	}

}

package com.cas.sim.tis.services.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.mapper.CatalogMapper;
import com.cas.sim.tis.services.CatalogService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class CatalogServiceImpl extends AbstractService<Catalog> implements CatalogService {

	@Override
	public List<Catalog> findSections(int type, Integer upperId) {
		Condition condition = new Condition(Catalog.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("lvl", type);
		criteria.andEqualTo("upperId", upperId);

		CatalogMapper sectionMapper = (CatalogMapper) mapper;
		sectionMapper.selectByCondition(condition);

		List<Catalog> sections = null;
		try {
			sections = sectionMapper.selectByCondition(condition);
			LOG.debug("查询到子节点数量：{}", sections.size());
		} catch (Exception e) {
			LOG.error("查询ID{}下子节点失败！", upperId);
			sections = Collections.emptyList();
		}
		return sections;
	}

}

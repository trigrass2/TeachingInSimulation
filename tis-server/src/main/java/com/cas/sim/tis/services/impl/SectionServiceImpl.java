package com.cas.sim.tis.services.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Section;
import com.cas.sim.tis.mapper.SectionMapper;
import com.cas.sim.tis.services.SectionService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class SectionServiceImpl extends AbstractService<Section> implements SectionService {

	@Override
	public List<Section> findSections(int type, Integer upperId) {
		Condition condition = new Condition(Section.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("lvl", type);
		criteria.andEqualTo("upperId", upperId);

		SectionMapper sectionMapper = (SectionMapper) mapper;
		sectionMapper.selectByCondition(condition);

		List<Section> sections = null;
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

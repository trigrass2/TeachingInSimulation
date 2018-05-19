package com.cas.sim.tis.services.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.mapper.CatalogMapper;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

	@Resource
	private CatalogMapper mapper;

	@Override
	public ResponseEntity findCatalogsByParentId(RequestEntity entity) {
		int parentId = entity.getInt("parentId");

		Condition condition = new Condition(Catalog.class);
		condition.selectProperties("id", "name", "type", "lessons", "rid");
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("rid", parentId);
		criteria.andEqualTo("del", 0);

		mapper.selectByCondition(condition);

		List<Catalog> catalogs = null;
		try {
			catalogs = mapper.selectByCondition(condition);
			log.debug("查询到子节点数量：{}", catalogs.size());
		} catch (Exception e) {
			log.error("查询ID{}下子节点失败！", parentId);
			catalogs = Collections.emptyList();
		}

		return ResponseEntity.success(catalogs);
	}

}

package com.cas.sim.tis.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.Preparation;
import com.cas.sim.tis.services.PreparationService;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;
@Service
public class PreparationServiceImpl extends AbstractService<Preparation> implements PreparationService {

	@Override
	public Preparation findPreparationByTaskIdAndCreator(Integer cid, int creator) {
		Condition condition = new Condition(Preparation.class);
		Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("catalogId", cid);
		criteria.andEqualTo("creator", creator);
		criteria.andEqualTo("del", 0);

		List<Preparation> preparations = findByCondition(condition);
		if (preparations.size() == 1) {
			return preparations.get(0);
		} else if (preparations.size() == 0) {
			return null;
		} else {
			LOG.warn("关联编号{}，创建人{}的有效备课信息应仅有一条！", cid, creator);
			return preparations.get(0);
		}
	}

	@Override
	public Preparation addPreparation(Preparation preparation) {
		int id = saveUseGeneratedKeys(preparation);
		return findById(id);
	}

}

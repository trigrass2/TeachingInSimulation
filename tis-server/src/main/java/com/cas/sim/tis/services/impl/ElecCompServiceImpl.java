package com.cas.sim.tis.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.mapper.ElecCompMapper;
import com.cas.sim.tis.services.ElecCompService;

@Service
public class ElecCompServiceImpl extends AbstractService<ElecComp> implements ElecCompService {

	@Override
	public Map<String, List<ElecComp>> findElecCompGroupByType() {
		ElecCompMapper dao = (ElecCompMapper) mapper;
		List<ElecComp> all = dao.selectAll();

		if (all == null) {
			return new HashMap<>();
		}

		return all.stream().collect(Collectors.groupingBy(ElecComp::getType));
	}
}

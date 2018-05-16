package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class ElecCompAction extends BaseAction {

	@Reference
	private ElecCompService service;

	public List<ElecComp> getElecCompList() {
		return service.findAll();
	}

	/**
	 * key:元器件型号
	 * @return
	 */
	public Map<Integer, List<ElecComp>> getElecCompMap() {
		return service.findElecCompGroupByType();
	}

	@Nullable
	public ElecComp getElecComp(String model) {
		return service.findElecCompByModel(model);
	}

	public ElecCompDef parse(String cfgPath) {
		return JaxbUtil.converyToJavaBean(HTTPUtils.getUrl(cfgPath), ElecCompDef.class);
	}

	public ElecComp findElecCompById(Integer id) {
		return service.findById(id);
	}

}

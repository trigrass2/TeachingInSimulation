package com.cas.sim.tis.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.cas.circuit.vo.ElecCompDef;
import com.cas.sim.tis.entity.ElecComp;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class ElecCompAction extends BaseAction {

	@Resource
	private ElecCompService service;

	public List<ElecComp> getElecCompList() {
		ResponseEntity resp = service.findElecComps();
		return JSON.parseArray(resp.data, ElecComp.class);
	}

	/**
	 * key:元器件型号
	 * @return
	 */
	public Map<Integer, List<ElecComp>> getElecCompMap() {
		ResponseEntity resp = service.findElecCompGroupByType();
		return JSON.parseObject(resp.data, new TypeReference<Map<Integer, List<ElecComp>>>() {});
	}

	@Nullable
	public ElecComp getElecComp(String model) {
		RequestEntity req = new RequestEntity();
		req.set("model", model).end();
		ResponseEntity resp = service.findElecCompByModel(req);
		return JSON.parseObject(resp.data, ElecComp.class);
	}

	public ElecCompDef parse(String cfgPath) {
		return JaxbUtil.converyToJavaBean(HTTPUtils.getUrl(cfgPath), ElecCompDef.class);
	}

	public ElecComp findElecCompById(Integer id) {
		RequestEntity req = new RequestEntity();
		req.set("id", id).end();
		ResponseEntity resp = service.findElecCompById(req);
		return JSON.parseObject(resp.data, ElecComp.class);
	}

}

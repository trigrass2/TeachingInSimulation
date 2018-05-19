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
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.xml.util.JaxbUtil;

@Component
public class ElecCompAction extends BaseAction {

	@Resource
	private ElecCompService service;

	/**
	 * 获得元器件列表
	 * @return 元器件对象列表
	 */
	public List<ElecComp> getElecCompList() {
		ResponseEntity resp = service.findElecComps();
		return JSON.parseArray(resp.data, ElecComp.class);
	}

	/**
	 * 获取所有元器件按元器件类型分类的Map集合<br>
	 * @return key:元器件型号<br>
	 *         value:元器件对象 List<ElecComp>
	 */
	public Map<Integer, List<ElecComp>> getElecCompMap() {
		ResponseEntity resp = service.findElecCompGroupByType();
		return JSON.parseObject(resp.data, new TypeReference<Map<Integer, List<ElecComp>>>() {});
	}

	/**
	 * 根据元器件型号获得元器件信息
	 * @param model 元器件型号
	 * @return 元器件对象
	 */
	@Nullable
	public ElecComp getElecComp(String model) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("model", model)//
				.build();
		ResponseEntity resp = service.findElecCompByModel(req);
		return JSON.parseObject(resp.data, ElecComp.class);
	}

	/**
	 * 根据元器件配置文件地址获得配置信息
	 * @param cfgPath 配置文件地址
	 * @return 元器件配置对象
	 */
	public ElecCompDef parse(String cfgPath) {
		return JaxbUtil.converyToJavaBean(HTTPUtils.getUrl(cfgPath), ElecCompDef.class);
	}

	/**
	 * 根据元器件编号获得的元器件对象
	 * @param id 元器件编号
	 * @return 元器件对象
	 */
	public ElecComp findElecCompById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findElecCompById(req);
		return JSON.parseObject(resp.data, ElecComp.class);
	}

}

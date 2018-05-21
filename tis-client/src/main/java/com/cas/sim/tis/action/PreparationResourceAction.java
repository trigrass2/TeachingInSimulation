package com.cas.sim.tis.action;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationResourceAction extends BaseAction {
	@Resource
	private PreparationResourceService service;

	/**
	 * 根据备课编号获得备课资源集合
	 * @param pid 备课编号
	 * @return List PreparationInfo集合
	 */
	public List<PreparationInfo> findResourcesByPreparationId(Integer pid) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("pid", pid)//
				.build();
		ResponseEntity resp = service.findResourcesByPreparationId(req);
		return JSON.parseArray(resp.data, PreparationInfo.class);
	}

	/**
	 * 根据备课资源编号获得备课资源对象
	 * @param id 备课资源编号
	 * @return 备课资源对象
	 */
	public PreparationResource findResourceById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findPreparationResourceById(req);
		return JSON.parseObject(resp.data, PreparationResource.class);
	}

	/**
	 * 逻辑删除备课资源对象信息
	 * @param id 备课资源对象编号
	 */
	public void deteleByLogic(Integer id) {
		PreparationResource resource = new PreparationResource();
		resource.setId(id);
		resource.setDel(true);
		resource.setUpdater(Session.get(Session.KEY_LOGIN_ID));

		RequestEntity req = new RequestEntityBuilder()//
				.set("resource", resource)//
				.build();
		service.updatePreparationResource(req);
	}

	/**
	 * 新增备课资源对象信息
	 * @param resources 备课资源对对象集合
	 */
	public void addResources(PreparationResource... resources) {
		addResources(Arrays.asList(resources));
	}

	/**
	 * 新增备课资源对象信息
	 * @param resources 备课资源对对象集合
	 */
	public void addResources(List<PreparationResource> resources) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resources", resources)//
				.build();
		service.savePreparationResources(req);
	}

}

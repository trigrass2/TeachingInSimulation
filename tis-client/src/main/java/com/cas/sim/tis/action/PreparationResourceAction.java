package com.cas.sim.tis.action;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.PreparationResource;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.vo.PreparationInfo;

@Component
public class PreparationResourceAction extends BaseAction {
	@Reference
	private PreparationResourceService service;

	public List<PreparationInfo> findResourcesByPreparationId(Integer pid) {
		return service.findResourcesByPreparationId(pid);
	}

	public PreparationResource findResourceById(Integer id) {
		return service.findById(id);
	}

	public void detele(Integer rid) {
		PreparationResource resource = service.findById(rid);
		resource.setDel(true);
		resource.setUpdater(Session.get(Session.KEY_LOGIN_ID));
		service.update(resource);
	}

	public void addResource(PreparationResource resource) {
		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.save(resource);
	}

	public void addResources(List<PreparationResource> resources) {
		service.save(resources);
	}

}

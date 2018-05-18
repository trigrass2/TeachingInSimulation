package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.cas.sim.tis.vo.ResourceInfo;
import com.cas.util.StringUtil;

@Component
public class ResourceAction extends BaseAction {
	@javax.annotation.Resource
	private ResourceService service;

//	public Integer addResource(Resource resource) {
//		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
//		return service.addResource(resource);
//	}

	public List<Integer> addResources(List<Resource> resources) {
		return service.addResources(resources);
	}

	public List<Resource> findResources(ResourceMenuType type, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, Integer creator) {
		if (ResourceMenuType.BROWSE == type) {
			return service.findResourcesByBrowseHistory(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		} else if (ResourceMenuType.COLLECTION == type) {
			return service.findResourcesByCollection(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		} else {
			return service.findResourcesByCreator(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		}
	}

	public int countResourceByType(ResourceMenuType menuType, int type, String keyword, Integer creator) {
		if (ResourceMenuType.BROWSE == menuType) {
			return service.countBrowseResourceByType(type, keyword, creator);
		} else if (ResourceMenuType.COLLECTION == menuType) {
			return service.countCollectionResourceByType(type, keyword, creator);
		} else {
			return service.countResourceByType(type, keyword, creator);
		}
	}

	public ResourceInfo findResourceInfoByID(int id) {
		return service.findResourceInfoByID(id);
	}

	public Resource findResourceByID(Integer id) {
		return service.findById(id);
	}

	public List<Resource> findResourcesByCreator(List<Integer> types, String keyword, Integer creator) {
		return service.findResourcesByCreator(types, keyword, creator);
	}

	public List<Resource> findResourcesByIds(List<String> ids) {
		if (StringUtils.isEmpty(ids)) {
			return new ArrayList<>();
		}
		List<Resource> resources = service.findByIds(StringUtil.combine(ids, ','));
		if (resources == null) {
			return new ArrayList<>();
		}
		return resources;
	}

	public void browsed(Integer id) {
		service.browsed(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void uncollect(Integer id) {
		if (id == null) {
			return;
		}
		service.uncollect(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void collected(Integer id) {
		if (id == null) {
			return;
		}
		service.collected(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void detele(Integer id) {
		service.deteleResource(id);
	}

}

package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.cas.sim.tis.vo.ResourceInfo;

@Component
public class ResourceAction extends BaseAction {
	@javax.annotation.Resource
	private ResourceService service;

//	public Integer addResource(Resource resource) {
//		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
//		return service.addResource(resource);
//	}

	public List<Integer> addResources(List<Resource> resources) {
		RequestEntity req = new RequestEntity()//
				.set("resources", resources)//
				.end();
		ResponseEntity resp = service.addResources(req);
		return JSON.parseArray(resp.data, Integer.class);
	}

	public List<Resource> findResources(ResourceMenuType type, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, Integer creator) {
		RequestEntity req = new RequestEntity()//
				.set("resourceTypes", resourceTypes)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.set("orderByClause", orderByClause)//
				.end();
//		分页信息
		req.pageNum = pagination;
		req.pageSize = pageSize;

		ResponseEntity resp;
		if (ResourceMenuType.BROWSE == type) {
			resp = service.findResourcesByBrowseHistory(req);
		} else if (ResourceMenuType.COLLECTION == type) {
			resp = service.findResourcesByCollection(req);
		} else {
			resp = service.findResourcesByCreator(req);
		}
		return JSON.parseArray(resp.data, Resource.class);
	}

	public int countResourceByType(ResourceMenuType menuType, int type, String keyword, Integer creator) {
		RequestEntity req = new RequestEntity()//
				.set("type", type)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.end();
		ResponseEntity resp;
		if (ResourceMenuType.BROWSE == menuType) {
			resp = service.countBrowseResourceByType(req);
		} else if (ResourceMenuType.COLLECTION == menuType) {
			resp = service.countCollectionResourceByType(req);
		} else {
			resp = service.countResourceByType(req);
		}
		return Integer.parseInt(resp.data);
	}

	public ResourceInfo findResourceInfoByID(int id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.end();
		ResponseEntity resp = service.findResourceInfoById(req);
		return JSON.parseObject(resp.data, ResourceInfo.class);
	}

	public Resource findResourceByID(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.end();
		ResponseEntity resp = service.findResourceById(req);
		return JSON.parseObject(resp.data, Resource.class);
	}

	public List<Resource> findResourcesByCreator(List<Integer> types, String keyword, Integer creator) {
		RequestEntity req = new RequestEntity()//
				.set("resourceTypes", types)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.end();
		ResponseEntity resp = service.findResourcesByCreator(req);
		return JSON.parseArray(resp.data, Resource.class);
	}

	public List<Resource> findResourcesByIds(List<String> ids) {
		if (StringUtils.isEmpty(ids)) {
			return new ArrayList<>();
		}
		RequestEntity req = new RequestEntity()//
				.set("ids", ids)//
				.end();

		ResponseEntity resp = service.findResourceByIds(req);
		return JSON.parseArray(resp.data, Resource.class);
	}

	public void browsed(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.end();
		service.findResourcesByCreator(req);
	}

	public void uncollect(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.end();
		service.uncollect(req);
	}

	public void collected(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.end();
		service.collected(req);
	}

	public void detele(Integer id) {
		RequestEntity req = new RequestEntity()//
				.set("id", id)//
				.end();
		service.deteleResource(req);
	}
}

package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.cas.sim.tis.vo.ResourceInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ResourceAction extends BaseAction {
	@javax.annotation.Resource
	private ResourceService service;

//	public Integer addResource(Resource resource) {
//		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
//		return service.addResource(resource);
//	}

	/**
	 * 批量新增资源
	 * @param resources 资源集合
	 * @return 返回新增资源的ID
	 */
	public List<Integer> addResources(List<Resource> resources) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resources", resources)//
				.build();
		ResponseEntity resp = service.addResources(req);
		return JSON.parseArray(resp.data, Integer.class);
	}

	public PageInfo<Resource> findResources(ResourceMenuType type, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resourceTypes", resourceTypes)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.set("orderByClause", orderByClause)//
				// 分页信息
				.pageNum(pagination)//
				.pageSize(pageSize)//
				.build();

		ResponseEntity resp;
		if (ResourceMenuType.BROWSE == type) {
			resp = service.findResourcesByBrowseHistory(req);
		} else if (ResourceMenuType.COLLECTION == type) {
			resp = service.findResourcesByCollection(req);
		} else {
			resp = service.findResourcesByCreator(req);
		}
		return JSON.parseObject(resp.data, new TypeReference<PageInfo<Resource>>() {});
	}

	public int countResourceByType(ResourceMenuType menuType, int type, String keyword, Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("type", type)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.build();
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

	public Map<Integer, Integer> countResourceByType(ResourceMenuType menuType, List<Integer> types, String keyword, int creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("types", types)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.build();
		ResponseEntity resp;
		if (ResourceMenuType.BROWSE == menuType) {
			resp = service.countBrowseResourceByTypes(req);
		} else if (ResourceMenuType.COLLECTION == menuType) {
			resp = service.countCollectionResourceByTypes(req);
		} else {
			resp = service.countResourceByTypes(req);
		}
		JSONObject obj = JSON.parseObject(resp.data);
		return null;
	}

	public ResourceInfo findResourceInfoByID(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findResourceInfoById(req);
		return JSON.parseObject(resp.data, ResourceInfo.class);
	}

	public Resource findResourceByID(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findResourceById(req);
		return JSON.parseObject(resp.data, Resource.class);
	}

	public List<Resource> findResourcesByCreator(List<Integer> types, String keyword, Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resourceTypes", types)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findResourcesByCreator(req);
		return JSON.parseArray(resp.data, Resource.class);
	}

	public List<Resource> findResourcesByIds(List<String> ids) {
		if (StringUtils.isEmpty(ids)) {
			return new ArrayList<>();
		}
		RequestEntity req = new RequestEntityBuilder()//
				.set("ids", ids)//
				.build();

		ResponseEntity resp = service.findResourceByIds(req);
		return JSON.parseArray(resp.data, Resource.class);
	}

	public void browsed(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		service.findResourceById(req);
	}

	public void uncollect(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.uncollect(req);
	}

	public void collected(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.collected(req);
	}

	public void detele(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		service.deteleResource(req);
	}

}

package com.cas.sim.tis.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
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

	/**
	 * 按条件分页查询资源集合
	 * @param type 菜单类型
	 * @param pageNum 查询页
	 * @param pageSize 查询条数
	 * @param resourceTypes 资源类型集合
	 * @param keyword 查询关键字
	 * @param orderByClause 排序条件
	 * @param creator 创建人
	 * @return PageInfo Resource资源集合
	 */
	public PageInfo<Resource> findResources(ResourceMenuType type, int pageNum, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resourceTypes", resourceTypes)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.set("orderByClause", orderByClause)//
				// 分页信息
				.pageNum(pageNum)//
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

	/**
	 * 按资源类型统计
	 * @param menuType 菜单类型
	 * @param type 资源类型
	 * @param keyword 查询关键字
	 * @param creator 创建人
	 * @return int 资源数量
	 */
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

	/**
	 * 按条件分页查询资源集合
	 * @param menuType 菜单类型
	 * @param types 资源类型集合
	 * @param keyword 查询关键字
	 * @param creator 创建人
	 * @return Map资源数量集合<br>
	 *         Key:资源类型<br>
	 *         Value:对应数量
	 */
	@Nonnull
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
		return JSON.parseObject(resp.data, new TypeReference<Map<Integer, Integer>>() {});
	}

	/**
	 * 根据资源编号查询资源相关信息
	 * @param id 资源编号
	 * @return 资源相关信息
	 */
	public ResourceInfo findResourceInfoById(int id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findResourceInfoById(req);
		return JSON.parseObject(resp.data, ResourceInfo.class);
	}

	/**
	 * 根据资源编号查询资源
	 * @param id 资源编号
	 * @return 资源
	 */
	public Resource findResourceById(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.build();
		ResponseEntity resp = service.findResourceById(req);
		return JSON.parseObject(resp.data, Resource.class);
	}

	/**
	 * 根据上传人编号按条件查询资源集合
	 * @param types 资源类型集合
	 * @param keyword 查询关键字
	 * @param creator 创建人
	 * @return List 资源集合
	 */
	public List<Resource> findResourcesByCreator(List<Integer> types, String keyword, Integer creator) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("resourceTypes", types)//
				.set("keyword", keyword) //
				.set("creator", creator)//
				.build();
		ResponseEntity resp = service.findResourcesByCreator(req);
		return JSON.parseArray(resp.data, Resource.class);
	}

	/**
	 * 根据资源编号集合查询资源集合
	 * @param ids 资源编号集合
	 * @return List 资源集合
	 */
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

	/**
	 * 添加浏览记录
	 * @param id 资源编号
	 */
	public void browsed(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.findResourceById(req);
	}

	/**
	 * 取消收藏记录
	 * @param id 资源编号
	 */
	public void uncollect(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.uncollect(req);
	}

	/**
	 * 添加收藏记录
	 * @param id 资源编号
	 */
	public void collected(Integer id) {
		RequestEntity req = new RequestEntityBuilder()//
				.set("id", id)//
				.set("creator", Session.get(Session.KEY_LOGIN_ID))//
				.build();
		service.collected(req);
	}

	/**
	 * 删除资源
	 * @param id 资源编号
	 */
	public void deteleByLogic(Integer id) {
		Resource resource = new Resource();
		resource.setId(id);
		resource.setDel(true);
		
		RequestEntity req = new RequestEntityBuilder()//
				.set("resource", resource)//
				.build();
		service.updateResource(req);
	}

}

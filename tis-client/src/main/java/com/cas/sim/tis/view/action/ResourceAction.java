package com.cas.sim.tis.view.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.view.control.imp.resource.ResourceList.ResourceMenuType;
import com.cas.sim.tis.vo.ResourceInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ResourceAction {
	@javax.annotation.Resource
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactory;

	public boolean addResource(Resource resource) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
		return service.addResource(resource);
	}

	public PageInfo<Resource> findResources(ResourceMenuType type, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, List<Integer> creators) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		if (ResourceMenuType.BROWSE == type) {
			return service.findResourcesByBrowseHistory(pagination, pageSize, resourceTypes, keyword, orderByClause, creators.get(0));
		} else if (ResourceMenuType.COLLECTION == type) {
			return service.findResourcesByCollection(pagination, pageSize, resourceTypes, keyword, orderByClause, creators.get(0));
		} else {
			return service.findResourcesByCreator(pagination, pageSize, resourceTypes, keyword, orderByClause, creators);
		}
	}

	public int countResourceByType(ResourceMenuType menuType, int type, String keyword, List<Integer> creators) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		if (ResourceMenuType.BROWSE == menuType) {
			return service.countBrowseResourceByType(type, keyword, creators.get(0));
		} else if (ResourceMenuType.COLLECTION == menuType) {
			return service.countCollectionResourceByType(type, keyword, creators.get(0));
		} else {
			return service.countResourceByType(type, keyword, creators);
		}
	}

	public ResourceInfo findResourceInfoByID(int id) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		return service.findResourceInfoByID(id);
	}

	public Resource findResourceByID(Integer id) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		return service.findById(id);
	}

	public void browsed(Integer id) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		service.browsed(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void uncollect(Integer id) {
		if (id == null) {
			return;
		}
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		service.uncollect(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void collected(Integer id) {
		if (id == null) {
			return;
		}
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		service.collected(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void detele(Integer id) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		service.deteleResource(id);
	}
}

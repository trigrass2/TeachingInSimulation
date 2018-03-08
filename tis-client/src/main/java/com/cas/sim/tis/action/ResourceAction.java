package com.cas.sim.tis.action;

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
public class ResourceAction extends BaseAction<ResourceService> {
	@javax.annotation.Resource
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactory;

	public Integer addResource(Resource resource) {
		ResourceService service = getService();
		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
		return service.addResource(resource);
	}

	public PageInfo<Resource> findResources(ResourceMenuType type, int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, Integer creator) {
		ResourceService service = getService();
		if (ResourceMenuType.BROWSE == type) {
			return service.findResourcesByBrowseHistory(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		} else if (ResourceMenuType.COLLECTION == type) {
			return service.findResourcesByCollection(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		} else {
			return service.findResourcesByCreator(pagination, pageSize, resourceTypes, keyword, orderByClause, creator);
		}
	}

	public int countResourceByType(ResourceMenuType menuType, int type, String keyword, Integer creator) {
		ResourceService service = getService();
		if (ResourceMenuType.BROWSE == menuType) {
			return service.countBrowseResourceByType(type, keyword, creator);
		} else if (ResourceMenuType.COLLECTION == menuType) {
			return service.countCollectionResourceByType(type, keyword, creator);
		} else {
			return service.countResourceByType(type, keyword, creator);
		}
	}

	public ResourceInfo findResourceInfoByID(int id) {
		return getService().findResourceInfoByID(id);
	}

	public Resource findResourceByID(Integer id) {
		return getService().findById(id);
	}

	public List<Resource> findResourcesByCreator(List<Integer> types, String keyword, Integer creator) {
		return getService().findResourcesByCreator(types, keyword, creator);
	}

	public void browsed(Integer id) {
		getService().browsed(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void uncollect(Integer id) {
		if (id == null) {
			return;
		}
		getService().uncollect(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void collected(Integer id) {
		if (id == null) {
			return;
		}
		getService().collected(id, Session.get(Session.KEY_LOGIN_ID));
	}

	public void detele(Integer id) {
		getService().deteleResource(id);
	}

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return resourceServiceFactory;
	}

}

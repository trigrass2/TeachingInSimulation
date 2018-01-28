package com.cas.sim.tis.view.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.vo.ResourceInfo;
import com.github.pagehelper.PageInfo;

@Component
public class ResourceAction {
	@javax.annotation.Resource
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactory;

	public void addResource(Resource resource) {
		if (resource == null) {
			return;
		}
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		resource.setCreator(Session.get(Session.KEY_LOGIN_ID));
		service.addResource(resource);
	}

	public PageInfo<Resource> findResourcesByCreator(int pagination, int pageSize, List<Integer> resourceTypes, String keyword, String orderByClause, List<Integer> creators) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		return service.findResourcesByCreator(pagination, pageSize, resourceTypes, keyword, orderByClause, creators);
	}

	public int countResourceByType(int type, String keyword, List<Integer> creators) {
		ResourceService service = (ResourceService) resourceServiceFactory.getObject();
		return service.countResourceByType(type, keyword, creators);
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
		service.browsed(id);
	}
}

package com.cas.sim.tis.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.services.BrowseHistoryService;

@Component
public class BrowseHistoryAction extends BaseAction<BrowseHistoryService> {
	@Resource
	@Qualifier("browseHistoryServiceFactory")
	private RmiProxyFactoryBean browseHistoryServiceFactory;

	@Override
	protected RmiProxyFactoryBean getRmiProxyFactoryBean() {
		return browseHistoryServiceFactory;
	}

}

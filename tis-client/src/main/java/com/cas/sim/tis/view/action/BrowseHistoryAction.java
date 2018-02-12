package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class BrowseHistoryAction {
	@Resource
	@Qualifier("browseHistoryServiceFactory")
	private RmiProxyFactoryBean browseHistoryServiceFactory;

}

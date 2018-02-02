package com.cas.sim.tis.view.action;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.stereotype.Component;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.entity.BrowseHistory;
import com.cas.sim.tis.services.BrowseHistoryService;

@Component
public class BrowseHistoryAction {
	@Resource
	@Qualifier("browseHistoryServiceFactory")
	private RmiProxyFactoryBean browseHistoryServiceFactory;

}

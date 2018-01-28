package com.cas.sim.tis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.cas.sim.tis.services.BrowseHistoryService;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.UserService;

@Configuration
@PropertySource("file:cfg.properties")
public class RMIConfig {

	private static final Logger LOG = LoggerFactory.getLogger(RMIConfig.class);

	@Value("${server.base.address}")
	private String host;

	@Value("${server.rmi.registry}")
	private String port;

	@Bean
	@Qualifier("userServiceFactory")
	public RmiProxyFactoryBean buildUserServiceFactory() {
		RmiProxyFactoryBean factoryBean = new RmiProxyFactoryBean();
		factoryBean.setServiceUrl("rmi://" + host + ":" + port + "/userService");

		LOG.info("远程访问用户信息路径：{}", factoryBean.getServiceUrl());
		factoryBean.setServiceInterface(UserService.class);
		return factoryBean;
	}

	@Bean
	@Qualifier("resourceServiceFactory")
	public RmiProxyFactoryBean buildResourceServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/resourceService");
		bean.setServiceInterface(ResourceService.class);
		return bean;
	}
	
	@Bean
	@Qualifier("collectionServiceFactory")
	public RmiProxyFactoryBean buildCollectionServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/collectionService");
		bean.setServiceInterface(CollectionService.class);
		return bean;
	}
	
	@Bean
	@Qualifier("browseHistoryServiceFactory")
	public RmiProxyFactoryBean buildBrowseHistoryServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/browseHistoryService");
		bean.setServiceInterface(BrowseHistoryService.class);
		return bean;
	}
}

package com.cas.sim.tis.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.cas.sim.tis.services.ResourceService;

@Configuration
@PropertySource(value = { "file:${user.dir}/client.properties" })
public class ClientRMIConfig {
	@Value("${server.address}")
	private String server;

	@Value("${server.rmi.port}")
	private Integer rmiPort;

	@Bean(name = "resourceServiceFactory")
	public RmiProxyFactoryBean initResourceServiceRmiProxyFactoryBean() {
		RmiProxyFactoryBean factoryBean = new RmiProxyFactoryBean();
		factoryBean.setServiceUrl("rmi://" + server + ":" + rmiPort + "/resourceService");
		LoggerFactory.getLogger(getClass()).info("资源远程访问路径：{}", factoryBean.getServiceUrl());
		factoryBean.setServiceInterface(ResourceService.class);
		return factoryBean;
	}
}

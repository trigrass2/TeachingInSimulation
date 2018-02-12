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
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.TypicalCaseService;
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
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/userService");

		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(UserService.class);
		return bean;
	}

	@Bean
	@Qualifier("classServiceFactory")
	public RmiProxyFactoryBean buildClassServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/classService");

		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(ClassService.class);
		return bean;
	}

	@Bean
	@Qualifier("resourceServiceFactory")
	public RmiProxyFactoryBean buildResourceServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/resourceService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(ResourceService.class);
		return bean;
	}

	@Bean
	@Qualifier("collectionServiceFactory")
	public RmiProxyFactoryBean buildCollectionServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/collectionService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(CollectionService.class);
		return bean;
	}

	@Bean
	@Qualifier("browseHistoryServiceFactory")
	public RmiProxyFactoryBean buildBrowseHistoryServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/browseHistoryService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(BrowseHistoryService.class);
		return bean;
	}

	@Bean
	@Qualifier("libraryServiceFactory")
	public RmiProxyFactoryBean buildLibraryServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(LibraryService.class);
		return bean;
	}

	@Bean
	@Qualifier("questionServiceFactory")
	public RmiProxyFactoryBean buildQuestionServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/questionService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(QuestionService.class);
		return bean;
	}

	@Bean
	@Qualifier("libraryPublishServiceFactory")
	public RmiProxyFactoryBean buildLibraryPublishServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryPublishService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(LibraryPublishService.class);
		return bean;
	}

	@Bean
	@Qualifier("libraryRecordServiceFactory")
	public RmiProxyFactoryBean buildLibraryRecordServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryRecordService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(LibraryRecordService.class);
		return bean;
	}

	@Bean
	@Qualifier("elecCompServiceFactory")
	public RmiProxyFactoryBean buildElecCompServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/elecCompService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(ElecCompService.class);
		return bean;
	}

	@Bean
	@Qualifier("typicalCaseServiceFactory")
	public RmiProxyFactoryBean buildTypicalCaseServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/typicalCaseService");
		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(TypicalCaseService.class);
		return bean;
	}
}

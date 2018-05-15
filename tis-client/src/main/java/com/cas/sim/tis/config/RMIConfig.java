package com.cas.sim.tis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

import com.cas.sim.tis.services.BrokenCaseService;
import com.cas.sim.tis.services.BrowseHistoryService;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.services.DrawService;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.services.GoalCoverageService;
import com.cas.sim.tis.services.GoalRelationshipService;
import com.cas.sim.tis.services.GoalService;
import com.cas.sim.tis.services.LibraryAnswerService;
import com.cas.sim.tis.services.LibraryPublishService;
import com.cas.sim.tis.services.LibraryRecordService;
import com.cas.sim.tis.services.LibraryService;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.services.PreparationService;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.services.UserService;

@Configuration
public class RMIConfig {

	private static final Logger LOG = LoggerFactory.getLogger(RMIConfig.class);

	@Value("${server.base.address}")
	private String host;

	@Value("${server.rmi.registry}")
	private String port;

	@Bean(name = "userService")
	public RmiProxyFactoryBean buildUserServiceFactory() {
		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
		bean.setServiceUrl("rmi://" + host + ":" + port + "/userService");

		LOG.info("远程访问路径：{}", bean.getServiceUrl());
		bean.setServiceInterface(UserService.class);
		return bean;
	}
//
//	@Bean("classService")
//	public RmiProxyFactoryBean buildClassServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/classService");
//
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(ClassService.class);
//		return bean;
//	}
//
//	@Bean("resourceService")
//	public RmiProxyFactoryBean buildResourceServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/resourceService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(ResourceService.class);
//		return bean;
//	}
//
//	@Bean("collectionService")
//	public RmiProxyFactoryBean buildCollectionServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/collectionService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(CollectionService.class);
//		return bean;
//	}
//
//	@Bean("browseHistoryService")
//	public RmiProxyFactoryBean buildBrowseHistoryServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/browseHistoryService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(BrowseHistoryService.class);
//		return bean;
//	}
//
//	@Bean("libraryService")
//	public RmiProxyFactoryBean buildLibraryServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(LibraryService.class);
//		return bean;
//	}
//
//	@Bean("questionService")
//	public RmiProxyFactoryBean buildQuestionServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/questionService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(QuestionService.class);
//		return bean;
//	}
//
//	@Bean("libraryPublishService")
//	public RmiProxyFactoryBean buildLibraryPublishServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryPublishService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(LibraryPublishService.class);
//		return bean;
//	}
//
//	@Bean("libraryRecordService")
//	public RmiProxyFactoryBean buildLibraryRecordServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryRecordService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(LibraryRecordService.class);
//		return bean;
//	}
//
//	@Bean("libraryAnswerService")
//	public RmiProxyFactoryBean buildLibraryAnswerServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/libraryAnswerService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(LibraryAnswerService.class);
//		return bean;
//	}
//
//	@Bean("typicalCaseService")
//	public RmiProxyFactoryBean buildTypicalCaseServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/typicalCaseService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(TypicalCaseService.class);
//		return bean;
//	}
//
//	@Bean("brokenCaseService")
//	public RmiProxyFactoryBean buildBrokenCaseServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/brokenCaseService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(BrokenCaseService.class);
//		return bean;
//	}
//
//	@Bean("drawService")
//	public RmiProxyFactoryBean buildDrawServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/drawService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(DrawService.class);
//		return bean;
//	}
//
//	@Bean("elecCompService")
//	public RmiProxyFactoryBean buildElecCompServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/elecCompService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(ElecCompService.class);
//		return bean;
//	}
//
//	@Bean("catalogService")
//	public RmiProxyFactoryBean buildCatalogServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/catalogService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(CatalogService.class);
//		return bean;
//	}
//
//	@Bean("preparationService")
//	public RmiProxyFactoryBean buildPreparationServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/preparationService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(PreparationService.class);
//		return bean;
//	}
//
//	@Bean("preparationResourceService")
//	public RmiProxyFactoryBean buildPreparationResourceServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/preparationResourceService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(PreparationResourceService.class);
//		return bean;
//	}
//
//	@Bean("preparationQuizService")
//	public RmiProxyFactoryBean buildPreparationQuizServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/preparationQuizService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(PreparationQuizService.class);
//		return bean;
//	}
//
//	@Bean("goalService")
//	public RmiProxyFactoryBean buildGoalServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/goalService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(GoalService.class);
//		return bean;
//	}
//
//	@Bean("goalRelationshipService")
//	public RmiProxyFactoryBean buildGoalRelationshipServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/goalRelationshipService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(GoalRelationshipService.class);
//		return bean;
//	}
//
//	@Bean("goalCoverageService")
//	public RmiProxyFactoryBean buildGoalCoverageServiceFactory() {
//		RmiProxyFactoryBean bean = new RmiProxyFactoryBean();
//		bean.setServiceUrl("rmi://" + host + ":" + port + "/goalCoverageService");
//		LOG.info("远程访问路径：{}", bean.getServiceUrl());
//		bean.setServiceInterface(GoalCoverageService.class);
//		return bean;
//	}
}

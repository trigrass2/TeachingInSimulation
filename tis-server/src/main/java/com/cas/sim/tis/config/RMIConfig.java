package com.cas.sim.tis.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.cas.sim.tis.services.BrokenCaseService;
import com.cas.sim.tis.services.BrowseHistoryService;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.services.ClassService;
import com.cas.sim.tis.services.CollectionService;
import com.cas.sim.tis.services.DrawService;
import com.cas.sim.tis.services.ElecCompService;
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

/**
 * 远程方法调用（RMI：Remote Method invoke）相关配置<br>
 * 相应地，客户端也要有套类似的配置{@link ClientRMIConfig}
 */
@Configuration
public class RMIConfig {
	@Resource
	private UserService userService;
	@Resource
	private ClassService classService;
	@Resource
	private ResourceService resourceService;
	@Resource
	private CollectionService collectionService;
	@Resource
	private BrowseHistoryService browseHistoryService;
	@Resource
	private DrawService drawService;
	@Resource
	private ElecCompService elecCompService;
	@Resource
	private TypicalCaseService typicalCaseService;
	@Resource
	private BrokenCaseService brokenCaseService;
	@Resource
	private LibraryService libraryService;
	@Resource
	private QuestionService questionService;
	@Resource
	private LibraryPublishService libraryPublishService;
	@Resource
	private LibraryRecordService libraryRecordService;
	@Resource
	private LibraryAnswerService libraryAnswerService;
	@Resource
	private CatalogService catalogService;
	@Resource
	private PreparationService preparationService;
	@Resource
	private PreparationResourceService preparationResourceService;
	@Resource
	private PreparationQuizService preparationQuizService;
	@Resource
	private GoalService goalService;
	@Resource
	private GoalRelationshipService goalRelationshipService;

	@Value("${server.rmi.registry}")
	private Integer registPort;
	@Value("${server.rmi.service}")
	private Integer servicePort;

	@Bean
	public RmiServiceExporter userServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(UserService.class);
		exporter.setServiceName("userService");
		exporter.setService(userService);

		exporter.setServicePort(servicePort);
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter classServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(ClassService.class);
		exporter.setServiceName("classService");
		exporter.setService(classService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter resourceServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(ResourceService.class);
		exporter.setServiceName("resourceService");
		exporter.setService(resourceService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter collectionServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(CollectionService.class);
		exporter.setServiceName("collectionService");
		exporter.setService(collectionService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter drawServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(DrawService.class);
		exporter.setServiceName("drawService");
		exporter.setService(drawService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter elecCompServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(ElecCompService.class);
		exporter.setServiceName("elecCompService");
		exporter.setService(elecCompService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter typicalCaseServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(TypicalCaseService.class);
		exporter.setServiceName("typicalCaseService");
		exporter.setService(typicalCaseService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter brokenCaseServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(BrokenCaseService.class);
		exporter.setServiceName("brokenCaseService");
		exporter.setService(brokenCaseService);

		exporter.setServicePort(servicePort);
		exporter.setRegistryPort(registPort);
		return exporter;
	}

	@Bean
	public RmiServiceExporter browseHistoryServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(BrowseHistoryService.class);
		exporter.setServiceName("browseHistoryService");
		exporter.setService(browseHistoryService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter libraryServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(LibraryService.class);
		exporter.setServiceName("libraryService");
		exporter.setService(libraryService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter questionServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(QuestionService.class);
		exporter.setServiceName("questionService");
		exporter.setService(questionService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
//		RegistryPort端口默认为1099，用户客户端访问时用的，rmi://host:RegistryPort/serviceName
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter libraryPublishServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(LibraryPublishService.class);
		exporter.setServiceName("libraryPublishService");
		exporter.setService(libraryPublishService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter libraryRecordServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(LibraryRecordService.class);
		exporter.setServiceName("libraryRecordService");
		exporter.setService(libraryRecordService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter libraryAnswerServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(LibraryAnswerService.class);
		exporter.setServiceName("libraryAnswerService");
		exporter.setService(libraryAnswerService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter catalogServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(CatalogService.class);
		exporter.setServiceName("catalogService");
		exporter.setService(catalogService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter preparationServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(PreparationService.class);
		exporter.setServiceName("preparationService");
		exporter.setService(preparationService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter preparationResourceServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(PreparationResourceService.class);
		exporter.setServiceName("preparationResourceService");
		exporter.setService(preparationResourceService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}

	@Bean
	public RmiServiceExporter preparationQuizServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(PreparationQuizService.class);
		exporter.setServiceName("preparationQuizService");
		exporter.setService(preparationQuizService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}
	
	@Bean
	public RmiServiceExporter goalServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(GoalService.class);
		exporter.setServiceName("goalService");
		exporter.setService(goalService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}
	
	@Bean
	public RmiServiceExporter goalRelationshipServiceExporter() {
		RmiServiceExporter exporter = new RmiServiceExporter();
		exporter.setServiceInterface(GoalRelationshipService.class);
		exporter.setServiceName("goalRelationshipService");
		exporter.setService(goalRelationshipService);

		if (servicePort != null) {
			exporter.setServicePort(servicePort);
		}
		if (registPort != null) {
			exporter.setRegistryPort(registPort);
		}
		return exporter;
	}
}

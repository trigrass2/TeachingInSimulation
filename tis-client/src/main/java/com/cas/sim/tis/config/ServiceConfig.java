package com.cas.sim.tis.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
import com.cas.sim.tis.services.PreparationLibraryService;
import com.cas.sim.tis.services.PreparationPublishService;
import com.cas.sim.tis.services.PreparationQuizService;
import com.cas.sim.tis.services.PreparationResourceService;
import com.cas.sim.tis.services.PreparationService;
import com.cas.sim.tis.services.QuestionService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.services.UserService;

import io.airlift.drift.client.DriftClient;
import io.airlift.drift.client.DriftClientFactory;

@Configuration
public class ServiceConfig {

	@Resource
	private DriftClientFactory clientFactory;

	@Bean
	public UserService userService() {
		DriftClient<UserService> service = clientFactory.createDriftClient(UserService.class);
		return service.get();
	}

	@Bean
	public ClassService classService() {
		DriftClient<ClassService> service = clientFactory.createDriftClient(ClassService.class);
		return service.get();
	}

	@Bean
	public ResourceService resourceService() {
		DriftClient<ResourceService> service = clientFactory.createDriftClient(ResourceService.class);
		return service.get();
	}

	@Bean
	public CollectionService collectionService() {
		DriftClient<CollectionService> service = clientFactory.createDriftClient(CollectionService.class);
		return service.get();
	}

	@Bean
	public BrowseHistoryService browseHistoryService() {
		DriftClient<BrowseHistoryService> service = clientFactory.createDriftClient(BrowseHistoryService.class);
		return service.get();
	}

	@Bean
	public LibraryService libraryService() {
		DriftClient<LibraryService> service = clientFactory.createDriftClient(LibraryService.class);
		return service.get();
	}

	@Bean
	public QuestionService questionService() {
		DriftClient<QuestionService> service = clientFactory.createDriftClient(QuestionService.class);
		return service.get();
	}

	@Bean
	public LibraryPublishService libraryPublishService() {
		DriftClient<LibraryPublishService> service = clientFactory.createDriftClient(LibraryPublishService.class);
		return service.get();
	}

	@Bean
	public LibraryRecordService libraryRecordService() {
		DriftClient<LibraryRecordService> service = clientFactory.createDriftClient(LibraryRecordService.class);
		return service.get();
	}

	@Bean
	public LibraryAnswerService libraryAnswerService() {
		DriftClient<LibraryAnswerService> service = clientFactory.createDriftClient(LibraryAnswerService.class);
		return service.get();
	}

	@Bean
	public TypicalCaseService typicalCaseService() {
		DriftClient<TypicalCaseService> service = clientFactory.createDriftClient(TypicalCaseService.class);
		return service.get();
	}

	@Bean
	public BrokenCaseService brokenCaseService() {
		DriftClient<BrokenCaseService> service = clientFactory.createDriftClient(BrokenCaseService.class);
		return service.get();
	}

	@Bean
	public DrawService drawService() {
		DriftClient<DrawService> service = clientFactory.createDriftClient(DrawService.class);
		return service.get();
	}

	@Bean
	public ElecCompService elecCompService() {
		DriftClient<ElecCompService> service = clientFactory.createDriftClient(ElecCompService.class);
		return service.get();
	}

	@Bean
	public CatalogService catalogService() {
		DriftClient<CatalogService> service = clientFactory.createDriftClient(CatalogService.class);
		return service.get();
	}

	@Bean
	public PreparationService preparationService() {
		DriftClient<PreparationService> service = clientFactory.createDriftClient(PreparationService.class);
		return service.get();
	}

	@Bean
	public PreparationResourceService preparationResourceService() {
		DriftClient<PreparationResourceService> service = clientFactory.createDriftClient(PreparationResourceService.class);
		return service.get();
	}

	@Bean
	public PreparationQuizService preparationQuizService() {
		DriftClient<PreparationQuizService> service = clientFactory.createDriftClient(PreparationQuizService.class);
		return service.get();
	}
	
	@Bean
	public PreparationLibraryService preparationLibraryService() {
		DriftClient<PreparationLibraryService> service = clientFactory.createDriftClient(PreparationLibraryService.class);
		return service.get();
	}
	
	@Bean
	public PreparationPublishService preparationPublishService() {
		DriftClient<PreparationPublishService> service = clientFactory.createDriftClient(PreparationPublishService.class);
		return service.get();
	}

	@Bean
	public GoalService goalService() {
		DriftClient<GoalService> service = clientFactory.createDriftClient(GoalService.class);
		return service.get();
	}

	@Bean
	public GoalRelationshipService goalRelationshipService() {
		DriftClient<GoalRelationshipService> service = clientFactory.createDriftClient(GoalRelationshipService.class);
		return service.get();
	}

	@Bean
	public GoalCoverageService coverageService() {
		DriftClient<GoalCoverageService> service = clientFactory.createDriftClient(GoalCoverageService.class);
		return service.get();
	}
}

package com.cas.sim.tis.config;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
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
import com.cas.sim.tis.services.BrokenPublishService;
import com.cas.sim.tis.services.BrokenRecordService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.TypicalCaseService;
import com.cas.sim.tis.services.UserService;
import com.google.common.collect.ImmutableSet;

import io.airlift.drift.codec.ThriftCodecManager;
import io.airlift.drift.server.DriftServer;
import io.airlift.drift.server.DriftService;
import io.airlift.drift.server.stats.NullMethodInvocationStatsFactory;
import io.airlift.drift.transport.netty.server.DriftNettyServerConfig;
import io.airlift.drift.transport.netty.server.DriftNettyServerTransportFactory;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;

@Configuration
public class SwiftConfig {
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
	private BrokenPublishService brokenPublishService;
	@Resource
	private BrokenRecordService brokenRecordService;
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
	private PreparationLibraryService preparationLibraryService;
	@Resource
	private PreparationPublishService preparationPublishService;
	@Resource
	private GoalService goalService;
	@Resource
	private GoalCoverageService goalCoverageService;
	@Resource
	private GoalRelationshipService goalRelationshipService;

	@Value(value = "${server.thrift.port}")
	private Integer thriftPort;

	@Bean
	public DriftServer thriftServer() {
		DriftNettyServerConfig config = new DriftNettyServerConfig();
		config.setPort(thriftPort);

		ByteBufAllocator allocator = new PooledByteBufAllocator();

		Set<DriftService> services = new HashSet<>();
		services.add(new DriftService(userService));
		services.add(new DriftService(classService));
		services.add(new DriftService(resourceService));
		services.add(new DriftService(collectionService));
		services.add(new DriftService(browseHistoryService));
		services.add(new DriftService(drawService));
		services.add(new DriftService(elecCompService));
		services.add(new DriftService(typicalCaseService));
		services.add(new DriftService(brokenCaseService));
		services.add(new DriftService(brokenPublishService));
		services.add(new DriftService(brokenRecordService));
		services.add(new DriftService(libraryService));
		services.add(new DriftService(questionService));
		services.add(new DriftService(libraryPublishService));
		services.add(new DriftService(libraryRecordService));
		services.add(new DriftService(libraryAnswerService));
		services.add(new DriftService(catalogService));
		services.add(new DriftService(preparationService));
		services.add(new DriftService(preparationResourceService));
		services.add(new DriftService(preparationQuizService));
		services.add(new DriftService(preparationLibraryService));
		services.add(new DriftService(preparationPublishService));
		services.add(new DriftService(goalService));
		services.add(new DriftService(goalCoverageService));
		services.add(new DriftService(goalRelationshipService));
		// create the server
		DriftServer driftServer = new DriftServer(//
				new DriftNettyServerTransportFactory(config, allocator), //
				new ThriftCodecManager(), //
				new NullMethodInvocationStatsFactory(), //
				services, //
				ImmutableSet.of());
		return driftServer;
	}
}

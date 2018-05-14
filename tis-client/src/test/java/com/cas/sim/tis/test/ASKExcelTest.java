package com.cas.sim.tis.test;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.CatalogType;
import com.cas.sim.tis.consts.GoalRelationshipType;
import com.cas.sim.tis.consts.GoalType;
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.entity.Goal;
import com.cas.sim.tis.entity.GoalRelationship;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.services.GoalRelationshipService;
import com.cas.sim.tis.services.GoalService;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ASKExcelTest {

	@Autowired
	@Qualifier("catalogServiceFactory")
	private RmiProxyFactoryBean catalogServiceFactory;
	@Autowired
	@Qualifier("goalServiceFactory")
	private RmiProxyFactoryBean goalServiceFactory;
	@Autowired
	@Qualifier("goalRelationshipServiceFactory")
	private RmiProxyFactoryBean goalRelationshipServiceFactory;

	private void importExcelToCatalog() {
		Object[][] datas = ExcelUtil.readExcelSheet("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\resources\\《工厂电气控制设备》ASK.xlsx", "工厂电气控制设备", 6);

		CatalogService service = (CatalogService) catalogServiceFactory.getObject();
		GoalService goalService = (GoalService) goalServiceFactory.getObject();
		GoalRelationshipService relationshipService = (GoalRelationshipService) goalRelationshipServiceFactory.getObject();

		Catalog subject = new Catalog();
		subject.setName("工厂电气控制设备");
		subject.setCreatorId(1);
		subject.setType(CatalogType.SUBJECT.getType());
		service.save(subject);
		subject = getLastestCatalog(service);

		Catalog projectCatalog = null;
		Catalog taskCatalog = null;
		int subjectCount = 0;
		int projectCount = 0;
		for (int i = 1; i < datas.length; i++) {
			Object[] data = datas[i];
			Object project = data[0];
			if (Util.notEmpty(project)) {
				if (projectCatalog != null) {
					projectCatalog.setLessons(projectCount);
					service.update(projectCatalog);
				}
				projectCatalog = new Catalog();
				projectCatalog.setLessons(1);
				projectCatalog.setCreatorId(1);
				projectCatalog.setRid(subject.getId());
				projectCatalog.setName(String.valueOf(project));
				projectCatalog.setType(CatalogType.PROJECT.getType());
				service.save(projectCatalog);
				projectCatalog = getLastestCatalog(service);
				projectCount = 0;
				subjectCount++;
				continue;
			}
			Object task = data[1];
			if (Util.notEmpty(task)) {
				taskCatalog = new Catalog();
				taskCatalog.setLessons(1);
				taskCatalog.setCreatorId(1);
				taskCatalog.setRid(projectCatalog.getId());
				taskCatalog.setName(String.valueOf(task));
				taskCatalog.setType(CatalogType.TASK.getType());
				service.save(taskCatalog);
				taskCatalog = getLastestCatalog(service);
				projectCount++;
				continue;
			}
			Object k = data[2];
			if (Util.notEmpty(k)) {
				Goal kGoal = new Goal();
				kGoal.setCreator(1);
				kGoal.setName(String.valueOf(k));
				kGoal.setType(GoalType.KNOWLEDGE.getType());
				goalService.save(kGoal);

				Goal goal = getLastestGoal(goalService);

				GoalRelationship relationship = new GoalRelationship();
				relationship.setRelationId(taskCatalog.getId());
				relationship.setGoalId(goal.getId());
				relationship.setType(GoalRelationshipType.TASK.getType());
				relationship.setCreator(1);
				relationshipService.save(relationship);
			}
			Object s = data[3];
			if (Util.notEmpty(s)) {
				Goal sGoal = new Goal();
				sGoal.setCreator(1);
				sGoal.setName(String.valueOf(s));
				sGoal.setType(GoalType.SKILL.getType());
				goalService.save(sGoal);

				Goal goal = getLastestGoal(goalService);

				GoalRelationship relationship = new GoalRelationship();
				relationship.setRelationId(taskCatalog.getId());
				relationship.setGoalId(goal.getId());
				relationship.setType(GoalRelationshipType.TASK.getType());
				relationship.setCreator(1);
				relationshipService.save(relationship);
			}
			Object a = data[4];
			if (Util.notEmpty(a)) {
				Goal aGoal = new Goal();
				aGoal.setCreator(1);
				aGoal.setName(String.valueOf(a));
				aGoal.setType(GoalType.ATTITUDE.getType());
				goalService.save(aGoal);
				
				Goal goal = getLastestGoal(goalService);

				GoalRelationship relationship = new GoalRelationship();
				relationship.setRelationId(taskCatalog.getId());
				relationship.setGoalId(goal.getId());
				relationship.setType(GoalRelationshipType.TASK.getType());
				relationship.setCreator(1);
				relationshipService.save(relationship);
			}
		}
		if (projectCatalog != null) {
			projectCatalog.setLessons(projectCount);
			service.update(projectCatalog);
		}
		subject.setLessons(subjectCount);
		service.update(subject);
	}

	private Catalog getLastestCatalog(CatalogService service) {
		List<Catalog> catalogs = service.findAll();
		Catalog catalog = catalogs.get(catalogs.size() - 1);
		return catalog;
	}

	private Goal getLastestGoal(GoalService service) {
		List<Goal> goals = service.findAll();
		Goal goal = goals.get(goals.size() - 1);
		return goal;
	}

	@Test
	public void test() {
		importExcelToCatalog();
	}
}

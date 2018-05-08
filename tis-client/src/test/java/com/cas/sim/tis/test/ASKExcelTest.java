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
import com.cas.sim.tis.entity.Catalog;
import com.cas.sim.tis.services.CatalogService;
import com.cas.sim.tis.util.ExcelUtil;
import com.cas.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ASKExcelTest {

	@Autowired
	@Qualifier("catalogServiceFactory")
	private RmiProxyFactoryBean catalogServiceFactory;

	private void importExcelToCatalog() {
		Object[][] datas = ExcelUtil.readExcelSheet("G:\\Workspace\\TeachingInSimulation\\TeachingInSimulation\\tis-client\\src\\test\\resources\\《工厂电气控制设备》ASK.xlsx", "工厂电气控制设备", 6);

		CatalogService service = (CatalogService) catalogServiceFactory.getObject();

		Catalog subject = new Catalog();
		subject.setName("工厂电气控制设备");
		subject.setLessons(1);
		subject.setCreatorId(1);
		subject.setType(CatalogType.SUBJECT.getType());
		service.save(subject);
		subject = getLastestCatalog(service);

		Catalog projectCatalog = null;
		Catalog taskCatalog = null;
		Catalog kCatalog = null;
		Catalog sCatalog = null;
		Catalog aCatalog = null;
		for (int i = 1; i <= datas.length; i++) {
			Object[] data = datas[i];
			Object project = data[0];
			if (Util.notEmpty(project)) {
				projectCatalog = new Catalog();
				projectCatalog.setLessons(1);
				projectCatalog.setCreatorId(1);
				projectCatalog.setRid(subject.getId());
				projectCatalog.setName(String.valueOf(project));
				projectCatalog.setType(CatalogType.PROJECT.getType());
				service.save(projectCatalog);
				projectCatalog = getLastestCatalog(service);
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
				continue;
			}
			Object k = data[2];
			if (Util.notEmpty(k)) {
				kCatalog = new Catalog();
				kCatalog.setLessons(1);
				kCatalog.setCreatorId(1);
				kCatalog.setRid(taskCatalog.getId());
				kCatalog.setName(String.valueOf(k));
				kCatalog.setType(CatalogType.KNOWLEDGE.getType());
				service.save(kCatalog);
			}
			Object s = data[3];
			if (Util.notEmpty(s)) {
				sCatalog = new Catalog();
				sCatalog.setLessons(1);
				sCatalog.setCreatorId(1);
				sCatalog.setRid(taskCatalog.getId());
				sCatalog.setName(String.valueOf(s));
				sCatalog.setType(CatalogType.SKILL.getType());
				service.save(sCatalog);
			}
			Object a = data[4];
			if (Util.notEmpty(a)) {
				aCatalog = new Catalog();
				aCatalog.setLessons(1);
				aCatalog.setCreatorId(1);
				aCatalog.setRid(taskCatalog.getId());
				aCatalog.setName(String.valueOf(a));
				aCatalog.setType(CatalogType.ATTITUDE.getType());
				service.save(sCatalog);
			}
		}
	}

	private Catalog getLastestCatalog(CatalogService service) {
		List<Catalog> catalogs = service.findAll();
		Catalog catalog = catalogs.get(catalogs.size() - 1);
		return catalog;
	}

	@Test
	public void test() {
		importExcelToCatalog();
	}
}

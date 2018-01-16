package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.SpringUtil;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITest {

	@Test
	public void testGetObject() throws Exception {
		StopWatch watch = new StopWatch();
		watch.start();
		ResourceService resourceService = (ResourceService) SpringUtil.getBean("resourceServiceFactory");
		Assert.assertNotNull(resourceService);
		User user = new Teacher();
		user.setId(2);
		PageInfo<com.cas.sim.tis.entity.Resource> page = resourceService.findResources(user, 0, 3, new ArrayList<>(Arrays.asList(1, 4)));
		watch.stop();
		System.out.println(watch.getTotalTimeMillis());
		page.getList().stream().forEach(System.out::println);
	}
}

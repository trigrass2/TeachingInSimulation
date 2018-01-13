package com.cas.sim.tis.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Resource;

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
import com.github.pagehelper.PageInfo;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITimeTest {
	@Resource
	private ResourceService resourceService;
	
	@Test
	public void testName() throws Exception {
		StopWatch watch = new StopWatch();
		watch.start();
		Assert.assertNotNull(resourceService);
		User user = new Teacher();
		user.setId(2);
		PageInfo<com.cas.sim.tis.entity.Resource> page = resourceService.findResources(user, 0, 3, new ArrayList<>(Arrays.asList(1, 4)));
		watch.stop();
		
		System.out.println(watch.getTotalTimeMillis());
	}
}

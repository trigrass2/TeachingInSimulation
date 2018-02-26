package com.cas.sim.tis.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.services.ResourceService;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITimeTest {
	@Resource
	private ResourceService resourceService;
	
	@Test
	public void testName() throws Exception {
//		StopWatch watch = new StopWatch();
//		watch.start();
//		Assert.assertNotNull(resourceService);
//		User user = new Teacher();
//		user.setId(2);
//		resourceService.findResources(user, 0, 3, new ArrayList<>(Arrays.asList(1, 4)));
//		watch.stop();
//		
//		System.out.println(watch.getTotalTimeMillis());
	}
}

package com.cas.sim.tis.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.services.ElecCompService;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITest {

	@Resource
	@Qualifier("userServiceFactory")
	private RmiProxyFactoryBean userServiceFactory;
	@Resource
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactory;
	@Resource
	@Qualifier("elecCompServiceFactory")
	private RmiProxyFactoryBean elecCompServiceFactory;

	@Test
	public void testGetObject() throws Exception {
		UserService userService = (UserService) userServiceFactory.getObject();
		Assert.assertNotNull(userService);
		userService.findAll().forEach(System.out::println);
		
		ResourceService resourceService = (ResourceService) resourceServiceFactory.getObject();
		Assert.assertNotNull(resourceService);
		resourceService.findAll().forEach(System.out::println);
		
		ElecCompService elecCompService = (ElecCompService) elecCompServiceFactory.getObject();
		Assert.assertNotNull(elecCompService);
		elecCompService.findAll().forEach(System.out::println);
	}
}

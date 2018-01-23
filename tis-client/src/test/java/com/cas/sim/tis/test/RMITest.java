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

	@Test
	public void testGetObject() throws Exception {
		UserService service = (UserService) userServiceFactory.getObject();
		service.findAll().forEach(System.out::println);
		Assert.assertNotNull(service);
		ResourceService resourceService = (ResourceService) resourceServiceFactory.getObject();
		resourceService.findAll().forEach(System.out::println);
		Assert.assertNotNull(service);
	}
}

package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.Student;
import com.cas.sim.tis.entity.Teacher;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.services.StudentService;
import com.github.pagehelper.PageInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RMITest {

	@Resource
	@Qualifier("resourceServiceFactory")
	private RmiProxyFactoryBean resourceServiceFactoryBean;
	@Resource
	@Qualifier("studentServiceFactory")
	private RmiProxyFactoryBean studentServiceFactoryBean;

	@Test
	public void testGetObject() throws Exception {
		StopWatch watch = new StopWatch();
		watch.start();
		ResourceService resourceService = (ResourceService) resourceServiceFactoryBean.getObject();
		Assert.assertNotNull(resourceService);
		User user = new Teacher();
		user.setId(2);
		PageInfo<com.cas.sim.tis.entity.Resource> page = resourceService.findResources(user, 0, 3, new ArrayList<>(Arrays.asList(1, 4)));
		watch.stop();
		
		System.out.println(watch.getTotalTimeMillis());
		
		page.getList().stream().forEach(System.out::println);

		StudentService studentService = (StudentService) studentServiceFactoryBean.getObject();
		Assert.assertNotNull(resourceService);

		List<Student> students = studentService.findAll();
		students.stream().forEach(System.out::println);
	}
}
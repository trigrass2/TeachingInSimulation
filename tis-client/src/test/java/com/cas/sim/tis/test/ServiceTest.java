package com.cas.sim.tis.test;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.cas.sim.tis.Application;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.services.UserService;
import com.cas.sim.tis.thrift.RequestEntity;
import com.cas.sim.tis.thrift.RequestEntityBuilder;
import com.cas.sim.tis.thrift.ResponseEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class ServiceTest {
	@Resource
	private UserService userService;

	@Test
	public void testUserService() throws Exception {
		System.out.println("ServiceTest.testUserService()" + System.currentTimeMillis());

		RequestEntity entity = new RequestEntityBuilder()//
		.set("id", 2)
		.build();
		ResponseEntity resp = userService.findUserById(entity);
		User user = JSON.parseObject(resp.data, User.class);
		System.out.println(user);
		System.out.println("ServiceTest.testUserService()" + System.currentTimeMillis());
	}
}

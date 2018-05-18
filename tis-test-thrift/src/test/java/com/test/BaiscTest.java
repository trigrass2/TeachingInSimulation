package com.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.AppDemo;
import com.cas.sim.tis.thrift.ThriftClient;
import com.cas.sim.tis.thrift.ThriftEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppDemo.class)
public class BaiscTest {
	@Resource
	private ThriftClient thriftClient;

	@Test
	public void testJson() throws Exception {
		thriftClient.open();
		ThriftEntity result = thriftClient.getUserService().login("11", "22");
		System.out.println(result);
		result = thriftClient.getClassService().findClassAll(0, 0);
		System.out.println(result);
		thriftClient.close();
	}
}

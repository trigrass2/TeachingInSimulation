package com.cas.sim.tis.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.consts.UIConsts;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class CustomPropertyTest {
	@Resource
	private UIConsts consts;

	@Test
	public void testRead() throws Exception {
		Assert.assertEquals("10", consts.getPagesize());
	}
}

package com.cas.sim.tis.test;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.services.ResourceService;
import com.cas.sim.tis.util.HTTPUtils;
import com.cas.sim.tis.util.MsgUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageTest {
	@Resource
	private ResourceService resourceService;

	@Test
	public void testMsgUtil() throws Exception {
		Assert.assertEquals("ok", MsgUtil.getMessage("button.ok"));
	}

	@Test
	public void testResourceAvaliable() throws Exception {
		resourceService.findAll().forEach(r -> HTTPUtils.getFullPath(r.getPath()));
	}
}

package com.cas.sim.tis.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cas.sim.tis.Application;
import com.cas.sim.tis.util.MsgUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MessageTest {
	@Test
	public void testMsgUtil() throws Exception {
		Assert.assertEquals("ok", MsgUtil.getMessage("button.ok"));
	}
}

package com.cas.sim.tis.test;

import org.junit.Assert;
import org.junit.Test;

import com.cas.sim.tis.consts.Session;

public class BasicTest {
	@Test
	public void testEnum() throws Exception {
		Assert.assertEquals("KEY_LOGIN_ACCOUNT", Session.KEY_LOGIN_ACCOUNT.name());
	}
}

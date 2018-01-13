package com.cas.sim.tis.test;

import org.junit.Test;

import com.cas.sim.tis.socket.ClientT;
import com.cas.sim.tis.socket.message.LoginMessage;

public class MessageTest {
	@Test
	public void testSend() throws Exception {
		ClientT.getIns().connect();

		ClientT.getIns().send(new LoginMessage());
	}
}

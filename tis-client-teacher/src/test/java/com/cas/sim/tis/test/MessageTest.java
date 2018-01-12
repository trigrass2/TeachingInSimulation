package com.cas.sim.tis.test;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.cas.sim.tis.entity.Resource;
import com.cas.sim.tis.socket.ClientT;
import com.cas.sim.tis.socket.message.ResourcesMessage;
import com.cas.sim.tis.socket.message.handler.ResourcesMessageHandler;
import com.jme3.network.serializing.Serializer;

public class MessageTest {
	@Test
	public void testSend() throws Exception {
		ClientT.getIns().connect();
		ClientT.getIns().registerMessageHandler(ResourcesMessage.class, new ResourcesMessageHandler());
		Serializer.registerClass(Resource.class);

		ResourcesMessage msg = new ResourcesMessage();
		msg.setPagination(0);
		msg.setResTypeList(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
		msg.setResourceList(new ArrayList<>(Arrays.asList(new Resource(), new Resource(), new Resource())));

		Thread.sleep(1000);

		ClientT.getIns().send(msg);
	}
}

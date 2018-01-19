package com.cas.sim.tis.message.handler;

import org.springframework.stereotype.Component;

import com.jme3.network.Client;
import com.jme3.network.message.SerializerRegistrationsMessage;

@Component
public class SerializerRegistrationsMessageHandler implements ClientHandler<SerializerRegistrationsMessage> {

	@Override
	public void execute(Client client, SerializerRegistrationsMessage m) throws Exception {
//		do nothing
	}

}

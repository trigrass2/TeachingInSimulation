package com.cas.sim.tis.socket.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.network.Client;
import com.jme3.network.Message;

public interface ClientHandler {
	Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

	void execute(Client client, Message m) throws Exception;
}

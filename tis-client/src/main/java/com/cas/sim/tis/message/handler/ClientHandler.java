package com.cas.sim.tis.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.network.Client;
import com.jme3.network.Message;

public interface ClientHandler<M extends Message> {
	Logger LOG = LoggerFactory.getLogger(ClientHandler.class);

	void execute(Client client, M m) throws Exception;
}

package com.cas.sim.tis.socket.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.network.HostedConnection;
import com.jme3.network.Message;

public interface ServerHandler {
	Logger LOG = LoggerFactory.getLogger(ServerHandler.class);

	void execute(HostedConnection source, Message m) throws Exception;
}

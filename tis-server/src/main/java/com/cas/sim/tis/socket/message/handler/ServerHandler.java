package com.cas.sim.tis.socket.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.network.HostedConnection;

public interface ServerHandler<M> {
	Logger LOG = LoggerFactory.getLogger(ServerHandler.class);

	void execute(HostedConnection source, M m) throws Exception;
}

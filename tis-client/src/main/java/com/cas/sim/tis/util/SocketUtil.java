package com.cas.sim.tis.util;

import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.listener.ClientMessageListener;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.Network;

public enum SocketUtil {
	INSTENCE;
	public static final Logger LOG = LoggerFactory.getLogger(SocketUtil.class);
	private Client client;

	public boolean connect(@NotNull String address, int port) {
		try {
			client = Network.connectToServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, address, port, -1);
		} catch (IOException e) {
			LOG.warn("连接失败{}:{}", address, port);
			return false;
		}
		client.addMessageListener(ClientMessageListener.INSTENCE);
		return true;
	}

	public void start() {
		if (client == null) {
			return;
		}
		if (client.isConnected()) {
			return;
		}
		client.start();
	}

	public void send(Message msg) {
		if (client == null) {
			return;
		}
		if (!client.isStarted()) {
			LOG.info("客户端尚未启动");
			return;
		}
		while (!client.isConnected()) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		client.send(msg);
		LOG.info("发送成功{}", msg);
	}
}

package com.cas.sim.tis.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.socket.message.handler.ServerHandler;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

public  class CoreServer implements IServer {
	private static final Logger LOG = LoggerFactory.getLogger(CoreServer.class);

	private static final CoreServer ins = new CoreServer();

	private List<HostedConnection> clients = new ArrayList<>();
	private int maxClientNum = 0;

	private Server server;

	private Map<Class<? extends Message>, ServerHandler> messageHandlerClass = new HashMap<>();

	public static CoreServer getIns() {
		return ins;
	}

	@Override
	public void start() {
		server.start();
		LOG.info("主服务器已启动..");
	}

	@Override
	public void stop() {
		server.close();
		LOG.info("主服务器已关闭！");
	}

	public void registerMessageHandler(Class<? extends Message> msgClass, ServerHandler handler) {
//		注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, handler);
	}

	public void unregisterMessageHandler(Class<? extends Message> msgClass) {
		messageHandlerClass.remove(msgClass);
	}

	public void setMaxClientNum(int node) {
		LOG.info("客户端连接上限为{}人", node);
		maxClientNum = node;
	}

	public int getMaxClientNum() {
		return maxClientNum;
	}

	public List<HostedConnection> getClients() {
		return clients;
	}

}

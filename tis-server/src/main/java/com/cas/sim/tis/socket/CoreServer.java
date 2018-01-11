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
import com.cas.sim.tis.socket.message.handler.ServerHandler;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

public final class CoreServer implements IServer {
	private static final Logger LOG = LoggerFactory.getLogger(CoreServer.class);

	private static final CoreServer ins = new CoreServer();

	private List<HostedConnection> clients = new ArrayList<>();
	private int maxClientNum = 0;

	private Server server;

	private Map<Class<? extends Message>, ServerHandler> messageHandlerClass = new HashMap<>();

	private CoreServer() {
		Properties prop = new Properties();
		int port = 9999;
		try {
			prop.load(Files.newInputStream(Paths.get("server.properties")));
			port = Integer.parseInt(prop.getProperty("server.port"));
		} catch (Exception e) {
			LOG.warn("解析文件server.properties失败！错误原因{}", e.getMessage());
		}

		try {
			server = Network.createServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, port, port);
			LOG.warn("主服务器地址：{}", InetAddress.getLocalHost());
			LOG.warn("主服务器监听在{}端口上", port);

			server.addConnectionListener(new ConnectionListener() {
				@Override
				public void connectionAdded(Server server, HostedConnection conn) {
				}

				@Override
				public void connectionRemoved(Server server, HostedConnection conn) {
					boolean success = clients.remove(conn);
					if (success) {
						String userCode = conn.getAttribute(Session.KEY_LOGIN_USER_ID);
						LOG.info("用户{}已断开连接, 当前客户端数量{}", userCode, clients.size());
					}
				}
			});

			server.addMessageListener(new MessageListener<HostedConnection>() {
				@Override
				public void messageReceived(HostedConnection client, Message m) {
					LOG.info("收到客户端{}的消息{}", client.getAddress(), m.getClass().getName());
					ServerHandler handler = messageHandlerClass.get(m.getClass());
					if (handler != null) {
						try {
							handler.execute(client, m);
							LOG.warn("服务器已经成功处理了消息:{}", m);
						} catch (Exception e) {
							LOG.warn("服务器在处理消息时出现了异常:{}", e.getMessage());
						}
					} else {
						LOG.error("服务器无法处理消息:{}, 原因是缺少相应的 implements ServerHandler类", m.getClass());
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

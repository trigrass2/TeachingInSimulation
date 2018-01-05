package com.cas.sim.tis.socket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.socket.message.handler.ClientHandler;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;

public final class ClientT {

	private static final Logger LOG = LoggerFactory.getLogger(ClientT.class);

	private Map<Class<? extends Message>, ClientHandler> messageHandlerClass = new HashMap<>();

	private static ClientT ins = new ClientT();

	private Client client;

	private ClientT() {
		Properties prop = new Properties();
		int port = 9999;
		String address = null;
		try {
//			获取配置文件内容：服务器地址和端口号
			prop.load(Files.newInputStream(Paths.get("client.properties")));

			address = prop.getProperty("server.address");
			port = Integer.parseInt(prop.getProperty("server.port"));
		} catch (Exception e) {
			LOG.warn("解析文件server.properties失败！错误原因{}", e.getMessage());
		}

		try {
			client = Network.connectToServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, address, port);
			LOG.warn("成功连接至服务器{}:{}", address, port);

			client.addMessageListener(new MessageListener<Client>() {
				@Override
				public void messageReceived(Client client, Message m) {
					LOG.info("收到服务器的消息{}", m.getClass().getName());
					ClientHandler handler = messageHandlerClass.get(m.getClass());
					if (handler != null) {
						try {
							handler.execute(client, m);
							LOG.warn("已经成功处理了消息");
						} catch (Exception e) {
							LOG.warn("在处理消息时出现了异常{}", e.getMessage());
						}
					} else {
						LOG.error("无法处理消息{}", m.getClass());
					}
				}
			});

		} catch (IOException e) {
			LOG.warn("连接服务器失败！信息[地址：{},端口：{}]", address, port);
		}
	}

	public static ClientT getIns() {
		return ins;
	}

	public void connect() {
		client.start();
	}

	public void disconnect() {
		if (client != null && client.isConnected()) {
			client.close();
		}
	}

	public void send(Message msg) {
		if (client.isConnected()) {
			LOG.info("向服务器发送消息{}", msg);
			client.send(msg);
		} else {
			LOG.warn("消息发送失败{}", msg);
		}
	}

	public void registerMessageHandler(Class<? extends Message> msgClass, ClientHandler handler) {
//		注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, handler);
	}

	public void unregisterMessageHandler(Class<? extends Message> msgClass) {
		messageHandlerClass.remove(msgClass);
	}

}

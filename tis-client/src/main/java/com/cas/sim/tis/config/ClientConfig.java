package com.cas.sim.tis.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.handler.ClientHandler;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.serializing.Serializer;

@Configuration
public class ClientConfig {
	private final static Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
	private Map<Class<? extends Message>, ClientHandler<Message>> messageHandlerClass = new HashMap<>();

	@Value(value = "${server.address}")
	private String address;

	@Value(value = "${server.port}")
	private Integer port;

	@Bean(name = "jmeClient")
	public Client buildClient() throws IOException {
		Client client = null;
		client = Network.connectToServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, address, port);
		LOG.warn("成功连接至服务器{}:{}", address, port);

		client.addMessageListener(new MessageListener<Client>() {
			@Override
			public void messageReceived(Client client, Message m) {
				LOG.info("收到服务器的消息{}", m.getClass().getName());
				ClientHandler<Message> handler = messageHandlerClass.get(m.getClass());
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
		LOG.warn("连接服务器失败！信息[地址：{},端口：{}]", address, port);
		return client;
	}

	@SuppressWarnings("unchecked")
	public void registerMessageHandler(Class<? extends Message> msgClass, ClientHandler<? extends Message> handler) {
//		注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, (ClientHandler<Message>) handler);
	}

}

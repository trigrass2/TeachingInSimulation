package com.cas.sim.tis.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.handler.ClientHandler;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;
import com.jme3.network.serializing.Serializer;

@Configuration
@PropertySource(value = { "classpath:interface.properties" })
public class ClientConfig {
	private final static Logger LOG = LoggerFactory.getLogger(ClientConfig.class);
	private Map<Class<? extends Message>, ClientHandler<Message>> messageHandlerClass = new HashMap<>();

	@Value(value = "${server.base.address}")
	private String address;

	@Value(value = "${server.base.port}")
	private Integer port;

	@Bean
	public NetworkClient buildClient() {
		NetworkClient client = null;
//		try {
			client = Network.createClient(SystemInfo.APP_NAME, SystemInfo.APP_VERSION);
//			LOG.info("成功连接至服务器{}:{}", address, port);
//		} catch (IOException e) {
//			LOG.error("连接服务器失败！服务器信息[地址：{},端口：{}]", address, port);
//			throw new RuntimeException(e.getMessage());
//		}

		client.addMessageListener(new MessageListener<Client>() {
			@Override
			public void messageReceived(Client client, Message m) {
				LOG.info("收到消息{}", m.getClass().getName());
				ClientHandler<Message> handler = messageHandlerClass.get(m.getClass());
				if (handler != null) {
					try {
						handler.execute(client, m);
						LOG.info("消息处理成功");
					} catch (Exception e) {
						LOG.warn("消息处理失败", e);
					}
				} else {
					LOG.error("无法处理消息{}，缺少相应的处理类，Eg:public class {} implements ClientHandler\\{\\}", m.getClass(), m.getClass().getSimpleName());
				}
			}
		});
		return client;
	}

	@SuppressWarnings("unchecked")
	public void registerMessageHandler(Class<? extends Message> msgClass, ClientHandler<? extends Message> handler) {
		// 注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, (ClientHandler<Message>) handler);
	}

}

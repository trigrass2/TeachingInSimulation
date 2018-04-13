package com.cas.sim.tis.message.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cas.sim.tis.message.handler.ClientHandler;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.serializing.Serializer;

public enum ClientMessageListener implements MessageListener<Client> {
	INSTENCE;
	private static final Logger LOG = LoggerFactory.getLogger(ClientMessageListener.class);

	private Map<Class<? extends Message>, ClientHandler<? extends Message>> messageHandlerClass = new HashMap<>();

	@Override
	public void messageReceived(Client client, Message m) {
		LOG.info("收到消息{}", m.getClass().getName());
		@SuppressWarnings("unchecked")
		ClientHandler<Message> handler = (ClientHandler<Message>) messageHandlerClass.get(m.getClass());
		if (handler != null) {
			try {
				handler.execute(client, m);
				LOG.info("消息已处理{}", m.getClass().getName());
			} catch (Exception e) {
				LOG.warn("消息处理失败", e);
			}
		} else {
			LOG.error("无法处理消息{}，缺少相应的处理类，Eg:public class {}Handler implements ClientHandler\\{\\}", m.getClass(), m.getClass().getSimpleName());
		}
	}

	public void registerMessageHandler(Class<? extends Message> msgClass, ClientHandler<? extends Message> handler) {
		// 注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, handler);
	}

}

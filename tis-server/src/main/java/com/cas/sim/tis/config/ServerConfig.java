package com.cas.sim.tis.config;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.message.handler.ServerHandler;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ServerConfig {

	private Server server;

	private List<HostedConnection> clients = new ArrayList<>();

	private Map<Class<? extends Message>, ServerHandler<Message>> messageHandlerClass = new HashMap<>();

	@Value(value = "${server.base.max-login}")
	private Integer maxLogin;
	@Value(value = "${server.base.port}")
	private Integer port;

	@Bean
	public Server createDataServer() throws IOException {
		server = Network.createServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, port, -1);
		log.info("主服务器地址：{}", InetAddress.getLocalHost());
		log.info("主服务器监听在{}端口上", port);

		server.addConnectionListener(new ConnectionListener() {
			@Override
			public void connectionAdded(Server server, HostedConnection conn) {
				log.info("用户{}已连接", conn.getAddress());
			}

			@Override
			public void connectionRemoved(Server server, HostedConnection conn) {
				boolean success = clients.remove(conn);
				if (success) {
					String account = conn.getAttribute(Session.KEY_LOGIN_ACCOUNT.name());
					log.info("用户{}已断开连接, 当前客户端数量{}", account, clients.size());
				}
			}
		});

		server.addMessageListener(new MessageListener<HostedConnection>() {
			@Override
			public void messageReceived(HostedConnection client, Message m) {
				log.info("收到客户端{}的消息{}", client.getAddress(), m.getClass().getName());
				ServerHandler<Message> handler = messageHandlerClass.get(m.getClass());
				if (handler != null) {
					try {
						handler.execute(client, m);
						log.debug("消息处理成功");
					} catch (Exception e) {
						log.warn("消息处理失败", e);
					}
				} else {
					log.error("无法处理消息{}，缺少相应的处理类，Eg:public class {} implements ServerHandler\\{\\}", m.getClass(), m.getClass().getSimpleName());
				}
			}
		});
		return server;
	}

	@SuppressWarnings("unchecked")
	public void registerMessageHandler(Class<? extends Message> msgClass, ServerHandler<? extends Message> handler) {
//		注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, (ServerHandler<Message>) handler);
	}

	public void close() {
		if (server != null) {
			server.close();
		}
	}

	public Server getServer() {
		return server;
	}

	public List<HostedConnection> getClients() {
		return clients;
	}

	public int getMaxLogin() {
		return maxLogin;
	}

	public void setMaxLogin(int maxLogin) {
		this.maxLogin = maxLogin;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}

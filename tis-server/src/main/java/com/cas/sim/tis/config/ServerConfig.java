package com.cas.sim.tis.config;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cas.sim.tis.consts.Session;
import com.cas.sim.tis.consts.SystemInfo;
import com.cas.sim.tis.entity.User;
import com.cas.sim.tis.message.handler.ServerHandler;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;

@Configuration
public class ServerConfig {
	private final static Logger LOG = LoggerFactory.getLogger(ServerConfig.class);

	private List<HostedConnection> clients = new ArrayList<>();

	private Map<Class<? extends Message>, ServerHandler<Message>> messageHandlerClass = new HashMap<>();

	@Value(value = "${server.max-login}")
	private Integer maxLogin;
	@Value(value = "${server.port}")
	private Integer port;

	@Bean
	public FtpServer createFtpServer() {
//		Apache Ftp Server 默认的编码是"UTF-8"
		FtpServerFactory serverFactory = new FtpServerFactory();
//		FTP服务连接配置
		ConnectionConfigFactory connectionConfigFactory = new ConnectionConfigFactory();
//     	允许匿名连接
		connectionConfigFactory.setAnonymousLoginEnabled(true);
		serverFactory.setConnectionConfig(connectionConfigFactory.createConnectionConfig());
//		
//		设置用户控制中心
		PropertiesUserManagerFactory propertiesUserManagerFactory = new PropertiesUserManagerFactory();
		propertiesUserManagerFactory.setFile(new File("users.properties"));
		serverFactory.setUserManager(propertiesUserManagerFactory.createUserManager());

//      配置FTP端口
		ListenerFactory listenerFactory = new ListenerFactory();
		listenerFactory.setPort(21); // 默认21， 可以按需配置

		serverFactory.addListener("default", listenerFactory.createListener());

		return serverFactory.createServer();
	}

	@Bean
	public Server createDataServer() throws IOException {
		Server server = null;
		server = Network.createServer(SystemInfo.APP_NAME, SystemInfo.APP_VERSION, port, port);
		LOG.info("主服务器地址：{}", InetAddress.getLocalHost());
		LOG.info("主服务器监听在{}端口上", port);

		server.addConnectionListener(new ConnectionListener() {
			@Override
			public void connectionAdded(Server server, HostedConnection conn) {
			}

			@Override
			public void connectionRemoved(Server server, HostedConnection conn) {
				boolean success = clients.remove(conn);
				if (success) {
					User user = conn.getAttribute(Session.KEY_LOGIN_USER);
					LOG.info("用户{}已断开连接, 当前客户端数量{}", user.getCode(), clients.size());
				}
			}
		});

		server.addMessageListener(new MessageListener<HostedConnection>() {
			@Override
			public void messageReceived(HostedConnection client, Message m) {
				LOG.info("收到客户端{}的消息{}", client.getAddress(), m.getClass().getName());
				ServerHandler<Message> handler = messageHandlerClass.get(m.getClass());
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
		return server;
	}

	@SuppressWarnings("unchecked")
	public void registerMessageHandler(Class<? extends Message> msgClass, ServerHandler<? extends Message> handler) {
//		注册jme消息
		Serializer.registerClass(msgClass);
		messageHandlerClass.put(msgClass, (ServerHandler<Message>) handler);
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
